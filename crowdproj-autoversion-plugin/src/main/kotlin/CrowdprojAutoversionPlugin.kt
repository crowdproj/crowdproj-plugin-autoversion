package com.crowdproj.plugin.autoversion

import org.ajoberstar.grgit.Grgit
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.buildscript
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.repositories

@Suppress("unused")
class CrowdprojAutoversionPlugin : Plugin<Project> {
    private lateinit var grgit: Grgit
    private lateinit var releaseRe: Regex
    private var newVersion: String? = null

    override fun apply(project: Project) = project.run {
        val cwpExtension = extensions.create<CrowdprojAutoversionPluginExtension>("autoversion")
        buildscript {
            repositories {
                mavenCentral()
//                maven { url = uri("https://plugins.gradle.org/m2/") }
            }
            dependencies {
                classpath("org.ajoberstar.grgit:grgit-gradle::5.2.0")
            }
        }
        pluginManager.apply("org.ajoberstar.grgit")
        afterEvaluate {
            println("COMPUTING VERSION")
            grgit = project.extensions.getByType(Grgit::class.java)
            releaseRe = cwpExtension.releaseRe.getOrElse(defaultReleaseRe)
            computeVersion()?.also {
                println("COMPUTED VERSION $it")
                newVersion = it
                version = it
                println("PROJECT VERSION is set to $it")
            }
        }
    }

    private fun computeVersion(): String? = grgit.branch.current()?.name?.let { br ->
        releaseRe.matchEntire(br)?.let {
            it.groups[1]?.value
        }?.let { verPref ->
            println("PREF: $verPref")
            val reVersion = versionRe
            grgit.tag.list()
                .map { it.name }
                .filter { it.startsWith(verPref) }
                .mapNotNull { verStr ->
                    reVersion.matchEntire(verStr)
                        ?.let { it.groups[1]?.value }
                        ?.split(".")
                        ?.map { it.toInt() }
                        ?.toIntArray()
                    //                                ?.also { println("VERSION: $it") }
                }
                .maxWithOrNull(VersionComparator)
                ?.also { it[it.size - 1]++ }
                ?.joinToString(".")
                ?: "$verPref.0"
        }
    }

    companion object {
        val defaultReleaseRe = "^release/([\\d.]+)(.*)?$".toRegex()
        private val versionRe = "^([\\d.]+)(.*)$".toRegex()
        private val VersionComparator = object : Comparator<IntArray> {
            override fun compare(p0: IntArray?, p1: IntArray?): Int {
                (0 until maxOf(p0?.size ?: 0, p1?.size ?: 0)).forEach { i ->
                    val x0 = p0?.getOrNull(i)
                    val x1 = p1?.getOrNull(i)
                    val cmp = when {
                        x0 == null && x1 == null -> 0
                        x0 == null && x1 != null -> -1
                        x0 != null && x1 == null -> 1
                        else -> x0!!.compareTo(x1!!) // !! is allowed due to previous checks
                    }
                    if (cmp != 0) {
                        return cmp
                    }
                }
                return 0
            }
        }
    }
}

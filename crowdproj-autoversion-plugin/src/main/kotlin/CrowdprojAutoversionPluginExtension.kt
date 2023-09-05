package com.crowdproj.plugin.autoversion

import org.gradle.api.provider.Property

interface CrowdprojAutoversionPluginExtension {
    val releaseRe: Property<Regex>
    val shoudIncrement: Property<Boolean>
}

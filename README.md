# crowdproj-plugin-autoversion

Gradle Plugin for autoversioning. It frees you from manual update of the version of your project.

## Installation

`buld.gradle.kts`:

```kotlin
plugins {
    id("com.crowdproj.plugin.autoversion") version "<release version>"
}
```

You can set up your release branch template with the help of a regular expression:

```kotlin
autoversion {
    releaseRe.set(Regex("^version/([\\d.]+)$")) // default is "^release/([\\d.]+)(.*)?$"
}
```

In the above example plugin will handle versioning for branches like `version/0.0`, `version/1.0`, etc. The default
template is for branches like `release/0.0-stable`, `release/0.1-dev`, etc. So the generated versions will
be `0.0.0`, `0.0.1`, `0.0.2`, etc. for `release/0.0-stable` and `0.1.0`, `0.1.1`, `0.1.2`, etc. for `release/0.1-dev`
branches.

**Attention!**

By default, this plugin just computes new version on the basis of existing git tags AND DOES NOT publish new tag to the
git repo. In order to publish git tag to the repository you have to add shomething like following into
you `build.gradle.kts`:

```kotlin
tasks {
    shadowJar {
        mustRunAfter(addGitVersionTag)
    }
}
```

if you wish only create new tag in local environment or

```kotlin
tasks {
    shadowJar {
        mustRunAfter(pushGitVersionTag)
    }
}
```

if you prefer push the computed tag into your git repository.

### No version increment mode

If you CI scenario supposes only manual version increment, you can switch off auto incrementing with the following
option:
```kotlin
autoversion {
    shouldIncrement.set(false)
}
```
In this case your Gradle scripts will use the highest version obtained from the tags of current branch.

## License
This plugin is published under the terms of [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

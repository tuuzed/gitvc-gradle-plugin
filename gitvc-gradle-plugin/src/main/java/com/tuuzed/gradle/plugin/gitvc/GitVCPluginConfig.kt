package com.tuuzed.gradle.plugin.gitvc

internal data class GitVCPluginConfig(
    var gitHome: String = "",
    var repoDir: String = "",
    var buildTag: String = "",
    var enableDirty: Boolean = true,
    var enableBranch: Boolean = false
)
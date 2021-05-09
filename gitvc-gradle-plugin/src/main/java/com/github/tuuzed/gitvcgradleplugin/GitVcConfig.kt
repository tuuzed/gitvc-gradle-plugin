package com.github.tuuzed.gitvcgradleplugin

internal data class GitVcConfig(
    val GIT_HOME: String? = null,
    val REPO_DIR: String? = null,
    val BUILD_TAG: String? = null,
    val ENABLE_BRANCH: Boolean = false,
    val ENABLE_DIRTY: Boolean = true,
)

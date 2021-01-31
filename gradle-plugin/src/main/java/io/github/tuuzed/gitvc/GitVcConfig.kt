package io.github.tuuzed.gitvc

internal data class GitVcConfig(
    var GIT_HOME: String? = null,
    var REPO_DIR: String? = null,
    var BUILD_TAG: String? = null,
    var ENABLE_BRANCH: Boolean = false,
    var ENABLE_DIRTY: Boolean = true,
)

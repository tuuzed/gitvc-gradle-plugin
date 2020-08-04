package com.tuuzed.gradle.plugin.gitvc

import org.gradle.api.Plugin
import org.gradle.api.Project

class GitVCPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = project.config
        val metadata = GitMetadata(config.gitHome, config.repoDir)
        val majorAndMinor = metadata.getMajorAndMinor(config.buildTag)
        val buildVersion = metadata.getCommitCount(config.buildTag)
        val lastCommitSha = metadata.lastCommitSha
        val lastCommitDate = metadata.lastCommitDate
        val currentBranch = metadata.currentBranch
        val versionCode = metadata.totalCommitCount
        val versionName = StringBuilder().apply {
            append(majorAndMinor.first).append(".")
            append(majorAndMinor.second).append(".")
            append(buildVersion)
            if (config.enableBranch && currentBranch.isNotEmpty()) {
                append(".").append(currentBranch)
            }
            if (config.enableDirty && metadata.isDirty) {
                append(".DIRTY")
            }
        }.toString()
        project.extensions.add(
            "GitVC", mapOf(
                "CONFIG" to config,
                "MAJOR_VERSION" to majorAndMinor.first,
                "MINOR_VERSION" to majorAndMinor.second,
                "BUILD_VERSION" to buildVersion,
                "CURRENT_BRANCH" to currentBranch,
                "VERSION_CODE" to versionCode,
                "VERSION_NAME" to versionName,
                "LAST_COMMIT_SHA" to lastCommitSha,
                "LAST_COMMIT_DATE" to lastCommitDate
            )
        )

    }

    private val Project.config: GitVCPluginConfig
        get() = kotlin.runCatching {
            val config = GitVCPluginConfig()
            val configParameters = project.extensions.getByName("GitVCPlugin")
            if (configParameters is Map<*, *>) {
                config.gitHome = (configParameters["gitHome"] as String?) ?: ""
                config.repoDir = (configParameters["repoDir"] as String?) ?: project.rootDir.absolutePath
                config.buildTag = (configParameters["buildTag"] as String?) ?: ""
                config.enableDirty = (configParameters["enableDirty"] as Boolean?) ?: true
                config.enableBranch = (configParameters["enableBranch"] as Boolean?) ?: false
            }
            config
        }.getOrNull() ?: GitVCPluginConfig()

}
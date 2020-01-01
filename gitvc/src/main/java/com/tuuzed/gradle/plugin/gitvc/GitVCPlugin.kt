@file:Suppress("LocalVariableName")

package com.tuuzed.gradle.plugin.gitvc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

class GitVCPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = getGitVCPluginConfig(project)
        val metadata = GitMetadata(config.gitHome, config.repoDir)

        val majorAndMinor = metadata.getMajorAndMinor(config.buildTag)
        val buildVersion = metadata.getCommitCount(config.buildTag)
        val lastCommitSha = metadata.lastCommitSha
        val lastCommitDate = metadata.lastCommitDate
        val currentBranch = metadata.currentBranch
        val versionCode = metadata.totalCommitCount
        val versionName = StringBuilder().apply {
            append(majorAndMinor.major).append(".")
            append(majorAndMinor.minor).append(".")
            append(buildVersion)
            if (config.enableBranch && currentBranch.isNotEmpty()) {
                append(".").append(currentBranch)
            }
            if (config.enableDirty && metadata.isDirty) {
                append(".DIRTY")
            }
        }.toString()

        val GitVC = mapOf(
            "CONFIG" to config,
            "MAJOR_VERSION" to majorAndMinor.major,
            "MINOR_VERSION" to majorAndMinor.minor,
            "BUILD_VERSION" to buildVersion,
            "CURRENT_BRANCH" to currentBranch,
            "VERSION_CODE" to versionCode,
            "VERSION_NAME" to versionName,
            "LAST_COMMIT_SHA" to lastCommitSha,
            "LAST_COMMIT_DATE" to lastCommitDate
        )
        project.extensions.add("GitVC", GitVC)

    }

    private fun getGitVCPluginConfig(project: Project): GitVCPluginConfig {
        val config = GitVCPluginConfig()
        try {
            val configMap = project.extensions.getByName("GitVCPlugin")
            if (configMap is Map<*, *>) {
                config.gitHome = (configMap["gitHome"] as String?) ?: ""
                config.repoDir = (configMap["repoDir"] as String?) ?: project.rootDir.absolutePath
                config.buildTag = (configMap["buildTag"] as String?) ?: ""
                config.enableDirty = (configMap["enableDirty"] as Boolean?) ?: true
                config.enableBranch = (configMap["enableBranch"] as Boolean?) ?: false
            }
        } catch (ignored: UnknownDomainObjectException) {
            // ignored
        }
        return config
    }
}
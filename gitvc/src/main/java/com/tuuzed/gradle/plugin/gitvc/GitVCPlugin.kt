@file:Suppress("LocalVariableName")

package com.tuuzed.gradle.plugin.gitvc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

class GitVCPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = getGitVCPluginConfig(project)
        val git = GitHelper(config.gitHome, config.repoDir)

        val majorAndMinor = git.getMajorAndMinor(config.buildTag)
        val buildVersion = git.getCommitCount(config.buildTag)
        val lastCommitSha = git.lastCommitSha
        val lastCommitDate = git.lastCommitDate
        val currentBranch = git.currentBranch
        val versionCode = git.totalCommitCount
        val versionName = StringBuilder().apply {
            append(majorAndMinor.major).append(".")
            append(majorAndMinor.minor).append(".")
            append(buildVersion)
            if (config.enableBranch && currentBranch.isNotEmpty()) {
                append(".").append(currentBranch)
            }
            if (config.enableDirty && git.isDirty) {
                append(".DIRTY")
            }
        }.toString()

        val GitVC = mapOf(
            Pair("CONFIG", config),
            Pair("MAJOR_VERSION", majorAndMinor.major),
            Pair("MINOR_VERSION", majorAndMinor.minor),
            Pair("BUILD_VERSION", buildVersion),
            Pair("CURRENT_BRANCH", currentBranch),
            Pair("VERSION_CODE", versionCode),
            Pair("VERSION_NAME", versionName),
            Pair("LAST_COMMIT_SHA", lastCommitSha),
            Pair("LAST_COMMIT_DATE", lastCommitDate)
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
package com.github.tuuzed.gitvcgradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.text.SimpleDateFormat
import java.util.*

class GitVcPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = project.config
        val commands = GitCommands(config.GIT_HOME, config.REPO_DIR, config.BUILD_TAG)
        val majorAndMinor = commands.majorAndMinor
        val buildVersion = commands.commitCount
        val lastCommitSha = commands.lastCommitSha
        val lastCommitDate = commands.lastCommitDate
        val currentBranch = commands.currentBranch
        val versionCode = commands.totalCommitCount
        val versionName = StringBuilder().apply {
            append(majorAndMinor.first).append(".")
            append(majorAndMinor.second).append(".")
            append(buildVersion)
            if (config.ENABLE_BRANCH && currentBranch.isNotEmpty()) {
                append(".").append(currentBranch)
            }
            if (config.ENABLE_DIRTY && commands.isDirty) {
                append(".DIRTY")
            }
        }.toString()
        val gitVc = mapOf(
            "CONFIG" to config,
            "MAJOR_VERSION" to majorAndMinor.first,
            "MINOR_VERSION" to majorAndMinor.second,
            "BUILD_VERSION" to buildVersion,
            "CURRENT_BRANCH" to currentBranch,
            "VERSION_CODE" to versionCode,
            "VERSION_NAME" to versionName,
            "LAST_COMMIT_SHA" to lastCommitSha,
            "LAST_COMMIT_DATE" to lastCommitDate,
            "BUILD_DATE" to SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZ", Locale.ENGLISH).format(Date())
        )
        project.extensions.add("GitVc", gitVc)
    }

    private val Project.config: GitVcConfig
        get() = kotlin.runCatching { extensions.getByName("GitVcConfig") }.getOrNull()?.let {
            if (it is Map<*, *>) {
                GitVcConfig(
                    GIT_HOME = it["GIT_HOME"]?.toString(),
                    REPO_DIR = it["REPO_DIR"]?.toString(),
                    BUILD_TAG = it["BUILD_TAG"]?.toString(),
                    ENABLE_BRANCH = it["ENABLE_BRANCH"]?.toString()?.toBoolean() ?: false,
                    ENABLE_DIRTY = it["ENABLE_DIRTY"]?.toString()?.toBoolean() ?: true
                )
            } else {
                GitVcConfig()
            }
        } ?: GitVcConfig()

}

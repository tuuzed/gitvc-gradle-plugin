package com.tuuzed.gradle.plugin.gitvc

import java.io.File

internal class GitMetadata(
    private val gitHome: String = "",
    private val contextDir: String = ""
) {

    fun getMajorAndMinor(buildTag: String): MajorAndMinor {
        val majorAndMinor = MajorAndMinor()
        val tagList = tagList
        if (tagList.isEmpty()) {
            return majorAndMinor
        }
        val tag = when {
            buildTag.isEmpty() -> tagList[0]
            tagList.contains(buildTag) -> buildTag
            else -> throw RuntimeException("标签($buildTag)不存在。")
        }
        try {
            val arrays = tag.split(".")
            majorAndMinor.major = arrays[0].toInt()
            majorAndMinor.minor = arrays[1].toInt()
        } catch (e: Exception) {
            // ignored
        }
        return majorAndMinor
    }

    fun getCommitCount(buildTag: String): Int {
        return try {
            val tagList = tagList
            if (tagList.isEmpty()) {
                return totalCommitCount
            }
            val command = when {
                buildTag.isEmpty() -> "$git rev-list ${tagList[0]}.. --count"
                tagList.contains(buildTag) -> {
                    when (val indexOf = tagList.indexOf(buildTag)) {
                        // 最新标签
                        0 -> "$git rev-list ${tagList[0]}.. --count"
                        else -> "$git rev-list ${tagList[indexOf]}..${tagList[indexOf - 1]} --count"
                    }
                }
                else -> throw RuntimeException("标签($buildTag)不存在。")
            }
            val commitCount = command.execute(dir = contextDir).text().trim()
            commitCount.toInt()
        } catch (e: Exception) {
            0
        }
    }

    val isDirty: Boolean
        get() = try {
            "$git status --short".execute(dir = contextDir).text().trim().isNotEmpty()
        } catch (e: Exception) {
            false
        }

    val lastCommitSha: String
        get() = try {
            "$git log -1 --pretty=%H".execute(dir = contextDir).text().trim()
        } catch (e: Exception) {
            ""
        }

    val lastCommitDate: String
        get() = try {
            "$git log -1 --pretty=%cd".execute(dir = contextDir).text().trim()
        } catch (e: Exception) {
            ""
        }

    val totalCommitCount: Int
        get() = try {
            "$git rev-list HEAD --count".execute(dir = contextDir).text().trim().toInt()
        } catch (e: Exception) {
            0
        }


    val tagList: List<String>
        get() = try {
            "$git tag --list".execute(dir = contextDir).text().let { item ->
                if (item.trim().isEmpty()) {
                    return@let emptyList()
                }
                return@let item.split("\n")
                    .mapNotNull {
                        try {
                            it.trim().toFloat()
                        } catch (ignored: NumberFormatException) {
                            null
                        }
                    }
                    .sortedByDescending { it }
                    .map { String.format("%.1f", it) }
            }
        } catch (e: Exception) {
            emptyList()
        }

    val currentBranch: String
        get() = try {
            "$git branch --list".execute(dir = contextDir).text().let {
                if (it.trim().isEmpty()) {
                    return@let ""
                }
                it.split("\n").forEach { item ->
                    if (item.startsWith("* ")) {
                        return item.replace("* ", "")
                    }
                }
                return@let ""
            }
        } catch (e: Exception) {
            ""
        }


    private val git: String
        get() = if (gitHome.isEmpty()) {
            "git"
        } else {
            "$gitHome${File.separator}git"
        }

}
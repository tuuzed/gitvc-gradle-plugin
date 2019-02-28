package com.tuuzed.gradle.plugin.gitvc

import java.io.File

internal class GitHelper(
    private val gitHome: String = "",
    private val contextDir: String = ""
) {

    fun getMajorAndMinor(buildTag: String): MajorAndMinor {
        val majorAndMinor = MajorAndMinor(0, 0)
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
        } catch (ignored: Exception) {
            // ignored
        }
        return majorAndMinor
    }

    val isDirty: Boolean
        get() = !"$git status --short".execute(dir = contextDir).text().trim().isEmpty()

    val lastCommitSha: String
        get() = "$git log -1 --pretty=%H".execute(dir = contextDir).text().trim()

    val lastCommitDate: String
        get() = "$git log -1 --pretty=%cd".execute(dir = contextDir).text().trim()

    val totalCommitCount: Int
        get() = try {
            "$git rev-list HEAD --count".execute(dir = contextDir).text().trim().toInt()
        } catch (ignored: NumberFormatException) {
            0
        }


    fun getCommitCount(buildTag: String): Int {
        val tagList = tagList
        if (tagList.isEmpty()) {
            return totalCommitCount
        }
        val command = when {
            buildTag.isEmpty() -> "$git rev-list ${tagList[0]}.. --count"
            tagList.contains(buildTag) -> {
                val indexOf = tagList.indexOf(buildTag)
                when (indexOf) {
                    // 最新标签
                    0 -> "$git rev-list ${tagList[0]}.. --count"
                    else -> "$git rev-list ${tagList[indexOf]}..${tagList[indexOf - 1]} --count"
                }
            }
            else -> throw RuntimeException("标签($buildTag)不存在。")
        }
        val commitCount = command.execute(dir = contextDir).text().trim()
        return try {
            commitCount.toInt()
        } catch (ignored: NumberFormatException) {
            0
        }
    }

    val tagList: List<String>
        get() = "$git tag --list".execute(dir = contextDir).text().let { item ->
            if (item.trim().isEmpty()) {
                return@let emptyList()
            }
            return@let item.split("\n")
                .map {
                    try {
                        item.trim().toFloat()
                    } catch (ignored: NumberFormatException) {
                        null
                    }
                }
                .filter { it != null }
                .sortedBy { it }
                .map { String.format("%.1f", it) }
        }

    val currentBranch: String
        get() = "$git branch --list".execute(dir = contextDir).text().let {
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


    private val git: String
        get() = if (gitHome.isEmpty()) {
            "git"
        } else {
            "$gitHome${File.separator}git"
        }

}
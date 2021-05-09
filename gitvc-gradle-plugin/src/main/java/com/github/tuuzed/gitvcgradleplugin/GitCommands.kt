package com.github.tuuzed.gitvcgradleplugin

import java.io.File
import kotlin.math.pow

internal class GitCommands(
    private val gitHome: String?,
    private val contextDir: String?,
    private val buildTag: String?,
) {

    private val git: String get() = if (gitHome.isNullOrEmpty()) "git" else "$gitHome${File.separator}git"

    val majorAndMinor: Pair<Int, Int> by lazy {
        if (tagList.isEmpty()) {
            return@lazy 0 to 0
        }
        val tag = when {
            buildTag.isNullOrEmpty() -> tagList[0]
            tagList.contains(buildTag) -> buildTag
            else -> throw RuntimeException("标签($buildTag)不存在。")
        }
        return@lazy kotlin.runCatching {
            tag.split(".").let { it[0].toInt() to it[1].toInt() }
        }.getOrNull() ?: 0 to 0
    }

    val commitCount: Int by lazy {
        kotlin.runCatching {
            if (tagList.isEmpty()) {
                return@lazy totalCommitCount
            }
            val command = when {
                buildTag.isNullOrEmpty() -> "$git rev-list ${tagList[0]}.. --count"
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
        }.getOrNull() ?: 0
    }

    val isDirty: Boolean by lazy {
        kotlin.runCatching {
            "$git status --short".execute(dir = contextDir).text().trim().isNotEmpty()
        }.getOrNull() ?: false
    }

    val lastCommitSha: String by lazy {
        kotlin.runCatching {
            "$git log -1 --pretty=%H".execute(dir = contextDir).text().trim()
        }.getOrNull() ?: ""
    }

    val lastCommitDate: String by lazy {
        kotlin.runCatching {
            "$git log -1 --pretty=%cd".execute(dir = contextDir).text().trim()
        }.getOrNull() ?: ""
    }

    val totalCommitCount: Int by lazy {
        kotlin.runCatching {
            "$git rev-list HEAD --count".execute(dir = contextDir).text().trim().toInt()
        }.getOrNull() ?: 0
    }

    val currentBranch: String by lazy {
        kotlin.runCatching {
            "$git branch --list".execute(dir = contextDir).text().let {
                if (it.trim().isEmpty()) {
                    return@let ""
                }
                it.split("\n").forEach { item ->
                    if (item.startsWith("* ")) {
                        return@let item.replace("* ", "")
                    }
                }
                return@let ""
            }
        }.getOrNull() ?: ""
    }

    val tagList: List<String> by lazy {
        kotlin.runCatching {
            "$git tag --list".execute(dir = contextDir).text().let { item ->
                if (item.trim().isEmpty()) {
                    emptyList()
                } else {
                    item.split("\n")
                        .asSequence()
                        .filter { it.trim().toFloatOrNull() != null }
                        .sortedByDescending {
                            it.toFloat() * 10.toDouble().pow(it.split(".")[1].length.toDouble())
                        }
                        .toList()

                }
            }
        }.getOrNull() ?: emptyList()
    }

}

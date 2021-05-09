@file:JvmName("-ExtendKt")

package com.github.tuuzed.gitvcgradleplugin

import java.io.File
import java.io.InputStreamReader

internal fun String.execute(
    envp: Array<String>? = null,
    dir: String?
) = Runtime.getRuntime().exec(this, envp, if (dir.isNullOrEmpty()) null else File(dir))!!

internal fun Process.text() = InputStreamReader(inputStream).readText()

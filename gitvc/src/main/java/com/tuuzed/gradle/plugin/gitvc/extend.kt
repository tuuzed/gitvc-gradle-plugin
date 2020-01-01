package com.tuuzed.gradle.plugin.gitvc

import java.io.File
import java.io.InputStreamReader

fun String.execute(
    envp: Array<String>? = null,
    dir: String = ""
) = Runtime.getRuntime().exec(this, envp, if (dir.isEmpty()) null else File(dir))!!

fun Process.text() = InputStreamReader(inputStream).readText()

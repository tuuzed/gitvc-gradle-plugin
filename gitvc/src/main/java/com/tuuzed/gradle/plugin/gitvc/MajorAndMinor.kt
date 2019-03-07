package com.tuuzed.gradle.plugin.gitvc

internal data class MajorAndMinor(
    var major: Int,
    var minor: Int
) {
    override fun toString() = "$major.$minor"
}
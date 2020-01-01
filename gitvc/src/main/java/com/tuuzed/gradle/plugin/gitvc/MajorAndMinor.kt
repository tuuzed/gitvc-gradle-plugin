package com.tuuzed.gradle.plugin.gitvc

data class MajorAndMinor(
    var major: Int = 0,
    var minor: Int = 0
) {
    override fun toString() = "$major.$minor"
}
package com.tuuzed.gradle.plugin.gitvc

import org.junit.Before
import org.junit.Test

class GitHelperTest {

    private val contextDir = ""


    private lateinit var git: GitHelper

    @Before
    fun setUp() {
        git = GitHelper(contextDir = contextDir)
    }

    @Test
    fun getMajorAndMinor() {
        println("getMajorAndMinor: ${git.getMajorAndMinor("")}")
    }

    @Test
    fun isDirty() {
        println("isDirty: ${git.isDirty}")
    }

    @Test
    fun getLastCommitSha() {
        println("getLastCommitSha: ${git.lastCommitSha}")
    }

    @Test
    fun getLastCommitDate() {
        println("getLastCommitDate: ${git.lastCommitDate}")
    }

    @Test
    fun getTotalCommitCount() {
        println("getTotalCommitCount: ${git.totalCommitCount}")
    }

    @Test
    fun getCommitCount() {
        println("getCommitCount: ${git.getCommitCount("")}")
    }

    @Test
    fun getTagList() {
        println("getTagList: ${git.tagList}")
    }

    @Test
    fun getCurrentBranch() {
        println("getCurrentBranch: ${git.currentBranch}")
    }

}
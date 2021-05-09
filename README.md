一个自动化版本号管理Gradle插件

[![](https://www.jitpack.io/v/tuuzed/gitvc-gradle-plugin.svg)](https://www.jitpack.io/#tuuzed/gitvc-gradle-plugin)

``` groovy

buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
        mavenCentral()
    }
    dependencies {
        classpath 'com.github.tuuzed:gitvc-gradle-plugin:1.1.0'
    }
    extensions.GitVcConfig = [
        "GIT_HOME"     : null,
        "REPO_DIR"     : null,
        "BUILD_TAG"    : null,
        "ENABLE_DIRTY" : true,
        "ENABLE_BRANCH": false,
    ]
}


apply plugin: "com.github.tuuzed.gitvc"

task test() {
//    [
//        "CONFIG"          : config,
//        "MAJOR_VERSION"   : majorAndMinor.first,
//        "MINOR_VERSION"   : majorAndMinor.second,
//        "BUILD_VERSION"   : buildVersion,
//        "CURRENT_BRANCH"  : currentBranch,
//        "VERSION_CODE"    : versionCode,
//        "VERSION_NAME"    : versionName,
//        "LAST_COMMIT_SHA" : lastCommitSha,
//        "LAST_COMMIT_DATE": lastCommitDate,
//        "BUILD_DATE"      : buildDate,
//    ]
    System.err.println(GitVc)
}

```

 

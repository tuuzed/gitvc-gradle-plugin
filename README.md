# gitvc-gradle-plugin

[ ![Download](https://api.bintray.com/packages/tuuzed/maven/io.github.tuuzed.gitvc%3Agradle-plugin/images/download.svg) ](https://bintray.com/tuuzed/maven/io.github.tuuzed.gitvc%3Agradle-plugin/_latestVersion)

### 如何使用?

``` groovy
buildscript {
    repositories {
        maven { url 'https://dl.bintray.com/tuuzed/maven' }
        mavenCentral()
    }
    dependencies {
        classpath "io.github.tuuzed.gitvc:gradle-plugin:0.3.1"
    }
    extensions.GitVcConfig = [
        "GIT_HOME"     : null,
        "REPO_DIR"     : "null",
        "BUILD_TAG"    : null,
        "ENABLE_DIRTY" : true,
        "ENABLE_BRANCH": false,
    ]
}


apply plugin: "io.github.tuuzed.gitvc"

task test() {
    // [
    //     "CONFIG"          : config,
    //     "MAJOR_VERSION"   : majorAndMinor.first,
    //     "MINOR_VERSION"   : majorAndMinor.second,
    //     "BUILD_VERSION"   : buildVersion,
    //     "CURRENT_BRANCH"  : currentBranch,
    //     "VERSION_CODE"    : versionCode,
    //     "VERSION_NAME"    : versionName,
    //     "LAST_COMMIT_SHA" : lastCommitSha,
    //     "LAST_COMMIT_DATE": lastCommitDate,
    // ]
    System.err.println(GitVc)
}
```

 

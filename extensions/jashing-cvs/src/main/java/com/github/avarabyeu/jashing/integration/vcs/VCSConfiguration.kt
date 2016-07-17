package com.github.avarabyeu.jashing.integration.vcs

/**
 * @author Andrei Varabyeu
 */
class VCSConfiguration {

    var svns: List<SvnConfig>? = null
    var gits: List<GitConfig>? = null

    open class VCSConfig {
        var name: String? = null
        var url: String? = null
    }

    class SvnConfig : VCSConfig() {
        var username: String? = null
        var password: String? = null
    }

    class GitConfig : VCSConfig() {
        var repoName: String? = null
        var branches: List<String>? = null
    }
}

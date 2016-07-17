package com.github.avarabyeu.jashing.integration.vcs

/**
 * VCS clients exceptions

 * @author Andrey Vorobyov
 */
class VCSClientException : RuntimeException {

    constructor(message: String) : super(message) {
    }

    constructor(message: String, cause: Throwable) : super(message, cause) {
    }
}

package com.github.avarabyeu.jashing.core

/**
 * Incorrect configuration exception

 * @author avarabyeu
 */
class IncorrectConfigurationException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}

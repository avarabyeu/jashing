package com.github.avarabyeu.jashing.integration.vcs

import java.time.Instant

/**
 * Some basic methods for [com.github.avarabyeu.jashing.integration.vcs.VCSClient]
 * Just delegate calls to appropriate methods

 * @author Andrey Vorobyov
 */
abstract class AbstractVCSClient : VCSClient {

    override fun getCommitsPerUser(from: Instant): Map<String, Long> {
        return getCommitsPerUser(from, null)
    }

    override fun getCommitsForPeriod(from: Instant): Long {
        return getCommitsForPeriod(from, null)
    }
}

package com.github.avarabyeu.jashing.integration.vcs

import java.time.Instant
import java.util.*

/**
 * Composite VCS client implementation. Merges all results into one

 * @author Andrey Vorobyov
 */
class CompositeVCSClient(private val delegates: Collection<VCSClient>) : AbstractVCSClient(), VCSClient {

    override fun getCommitsPerUser(from: Instant, to: Instant?): Map<String, Long> {
        val results = ArrayList<Map<String, Long>>(delegates.size)
        results.addAll(delegates.map({ delegate -> delegate.getCommitsPerUser(from, to) }))
        return results.map({ it.entries })          // converts each map into an entry set
                .flatMap({ it.asIterable() }) // converts each set into an entry stream, then
                .groupBy({ it.key }, { it.value })
                .mapValues { entry -> entry.value.sum() }

    }


    override fun getCommitsForPeriod(from: Instant, to: Instant?): Long {
        return delegates.map({ delegate -> delegate.getCommitsForPeriod(from, to) }).sum()
    }

}

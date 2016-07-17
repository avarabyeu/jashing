package com.github.avarabyeu.jashing.integration.vcs

import java.time.Instant

/**
 * Basic operations with VCS systems

 * @author Andrey Vorobyov
 */
interface VCSClient {

    /**
     * Returns commits count per user for specified time period

     * @param from start time
     * *
     * @param to   end time
     * *
     * @return `{Map&lt;User name, Commits count&gt;&lt;/User&gt;}`
     */
    fun getCommitsPerUser(from: Instant, to: Instant?): Map<String, Long>

    /**
     * Returns commits count per user from specified time till now

     * @param from start time
     * *
     * @return `{Map&lt;User name, Commits count&gt;&lt;/User&gt;}`
     */
    fun getCommitsPerUser(from: Instant): Map<String, Long>

    /**
     * Returns commits count for all users for specified time period

     * @param from start time
     * *
     * @param to   end time
     * *
     * @return commits count
     */
    fun getCommitsForPeriod(from: Instant, to: Instant?): Long

    /**
     * Returns commits count for all users from specified specified time till now

     * @param from start time
     * *
     * @return commits count
     */
    fun getCommitsForPeriod(from: Instant): Long
}

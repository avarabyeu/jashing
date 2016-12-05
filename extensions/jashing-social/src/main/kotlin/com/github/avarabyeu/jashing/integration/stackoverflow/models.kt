package com.github.avarabyeu.jashing.integration.stackoverflow

import java.time.Instant

data class Owner(
        val userId: Long,
        val reputation: Long,
        var displayName: String,
        var link: String,
        val userType: String?,
        var profileImage: String?
)

data class Question(
        val questionId: Long,
        val link: String,
        val title: String,
        val tags: List<String>,
        val owner: Owner,
        val isAnswered: Boolean,
        val viewCount: Long,
        val answerCount: Long,
        val score: Long,
        val lastActivityDate: Instant?,
        val creationDate: Instant,
        val closedDate: Instant?
)

data class StackExchangeResponse<out T>(
        val items: List<T>,
        val isHasMore: Boolean = false,
        val quotaMax: Long,
        val quotaRemaining: Long
)

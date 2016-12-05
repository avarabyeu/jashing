package com.github.avarabyeu.jashing.integration.stackoverflow

import com.github.avarabyeu.restendpoint.http.HttpMethod
import com.github.avarabyeu.restendpoint.http.annotation.Path
import com.github.avarabyeu.restendpoint.http.annotation.Query
import com.github.avarabyeu.restendpoint.http.annotation.Request

/**
 * @author Andrei Varabyeu
 */
interface StackExchangeClient {

    /**
     * Obtains all questions marked with tag

     * @param filter Query filter
     */
    @Request(method = HttpMethod.GET, url = "/questions")
    fun getQuestions(@Query filter: Map<String, String>): StackExchangeResponse<Question>

    /**
     * Obtains all questions marked with tag
     */
    @Request(method = HttpMethod.GET, url = "/questions?tagged={tag}&filter=default&site=stackoverflow")
    fun getTaggedQuestions(@Path("tag") tag: String): StackExchangeResponse<Question>
}

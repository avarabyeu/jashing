package com.github.avarabyeu.jashing.integration.stackoverflow;

import com.github.avarabyeu.jashing.integration.stackoverflow.model.Question;
import com.github.avarabyeu.jashing.integration.stackoverflow.model.StackExchangeResponse;
import com.github.avarabyeu.restendpoint.http.HttpMethod;
import com.github.avarabyeu.restendpoint.http.annotation.Path;
import com.github.avarabyeu.restendpoint.http.annotation.Query;
import com.github.avarabyeu.restendpoint.http.annotation.Request;

import java.util.Map;

/**
 * @author Andrei Varabyeu
 */
public interface StackExchangeClient {

    /**
     * Obtains all questions marked with tag
     *
     * @param filter Query filter
     */
    @Request(method = HttpMethod.GET, url = "/questions")
    StackExchangeResponse<Question> getQuestions(@Query Map<String, String> filter);

    /**
     * Obtains all questions marked with tag
     */
    @Request(method = HttpMethod.GET, url = "/questions?tagged={tag}&filter=default&site=stackoverflow")
    StackExchangeResponse<Question> getTaggedQuestions(@Path("tag") String tag);
}

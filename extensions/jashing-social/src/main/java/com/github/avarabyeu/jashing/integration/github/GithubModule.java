package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.http.HttpRequest;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HttpContext;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * GitHub client configuration module
 *
 * @author avarabyeu
 */
public class GithubModule extends AbstractModule {

    public static final String GITHUB_API_URL_DEFAULT = "https://api.github.com";
    public static final String GITHUB_OAUTH_TOKEN = "github.oauth.token";

    @Override
    protected void configure() {

    }

    @Singleton
    @Provides
    public GitHubClient provideJiraClient(@Named(GITHUB_OAUTH_TOKEN) String oauthToken) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(oauthToken),
                "OAuth Token is null or empty. Please, check your configuration");

        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                .addInterceptorLast((HttpRequest request, HttpContext context) -> {
            /* add OAuth authorization header */
                    request.addHeader(HttpHeaders.AUTHORIZATION, "token ".concat(oauthToken));
                    request.addHeader("Accept", "application/vnd.github.v2+json");

            /* Make sure timestamps are returned in local timezone */
                    request.addHeader("Time-Zone", TimeZone.getDefault().getID());
                }).build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> ZonedDateTime
                                .parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        return RestEndpoints.create()
                .withHttpClient(httpClient)
                .withBaseUrl(GITHUB_API_URL_DEFAULT)
                .withSerializer(new GsonSerializer(gson))
                .forInterface(GitHubClient.class);
    }
}

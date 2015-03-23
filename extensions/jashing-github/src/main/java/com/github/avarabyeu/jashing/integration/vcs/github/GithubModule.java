package com.github.avarabyeu.jashing.integration.vcs.github;

import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.google.common.base.Optional;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.http.HttpRequest;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HttpContext;

import java.util.TimeZone;

/**
 * GitHub client configuration module
 *
 * @author avarabyeu
 */
public class GithubModule extends AbstractModule {

    public static final String GITHUB_API_URL = "github.api.url";
    public static final String GITHUB_API_URL_DEFAULT = "https://api.github.com";
    public static final String GITHUB_OAUTH_TOKEN = "github.oauth.token";

    @Override
    protected void configure() {

    }


    @Singleton
    @Provides
    public GitHubClient provideJiraClient(@Named(GITHUB_API_URL) Optional<String> githubApiUrl, @Named(GITHUB_OAUTH_TOKEN) Optional<String> oauthToken) {
        //Preconditions.checkArgument(!Strings.isNullOrEmpty(oauthToken), "OAuth Token is null or empty. Please, check your configuration");

        String defaultTimeZoneId = TimeZone.getDefault().getID();
        String oAuthToken = "token ".concat(oauthToken.get());

        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().addInterceptorLast((HttpRequest request, HttpContext context) -> {
            /* add OAuth authorization header */
            request.addHeader("Authorization", oAuthToken);

            /* Make sure timestamps are returned in local timezone */
            request.addHeader("Time-Zone", defaultTimeZoneId);
        }).build();

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


        //@Named("github.api.url") String githubApiUrl, @Named("github.oauth.token") String oauthToken
        return RestEndpoints.create()
                .withHttpClient(httpClient)
                .withBaseUrl(githubApiUrl.or(GITHUB_API_URL_DEFAULT))
                .withSerializer(new GsonSerializer(gson))
                .forInterface(GitHubClient.class);
    }
}

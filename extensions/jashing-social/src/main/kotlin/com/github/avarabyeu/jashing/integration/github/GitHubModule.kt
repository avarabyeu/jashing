package com.github.avarabyeu.jashing.integration.github

import com.github.avarabyeu.restendpoint.http.RestEndpoints
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer
import com.google.common.base.Preconditions
import com.google.common.base.Strings
import com.google.common.net.HttpHeaders
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import org.apache.http.HttpRequest
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.protocol.HttpContext
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

/**
 * Created by avarabyeu on 12/4/16.
 */
class GithubModule : AbstractModule() {

    override fun configure() {

    }

    @Singleton
    @Provides
    fun provideGithubClient(@Named(GITHUB_OAUTH_TOKEN) oauthToken: String): GitHubClient {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(oauthToken),
                "OAuth Token is null or empty. Please, check your configuration")

        val httpClient = HttpAsyncClients.custom()
                .addInterceptorLast { request: HttpRequest, context: HttpContext ->
                    /* add OAuth authorization header */
                    request.addHeader(HttpHeaders.AUTHORIZATION, "token " + oauthToken)
                    request.addHeader("Accept", "application/vnd.github.v2+json")

                    /* Make sure timestamps are returned in local timezone */
                    request.addHeader("Time-Zone", TimeZone.getDefault().id)
                }.build()

        val gson = GsonBuilder()
                .registerTypeAdapter(object : TypeToken<LocalDateTime>() {}.type,
                        JsonDeserializer { jsonElement: JsonElement?, type: Type?, jsonDeserializationContext: JsonDeserializationContext? ->
                            ZonedDateTime
                                    .parse(jsonElement!!.asJsonPrimitive.asString).toLocalDateTime()
                        })
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

        return RestEndpoints.create()
                .withHttpClient(httpClient)
                .withBaseUrl(GITHUB_API_URL_DEFAULT)
                .withSerializer(GsonSerializer(gson))
                .forInterface(GitHubClient::class.java)
    }

    companion object {

        const val GITHUB_API_URL_DEFAULT = "https://api.github.com"
        const val GITHUB_OAUTH_TOKEN = "github.oauth.token"
    }
}

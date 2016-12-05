package com.github.avarabyeu.jashing.integration.stackoverflow

import com.github.avarabyeu.restendpoint.http.RestEndpoints
import com.github.avarabyeu.restendpoint.http.exception.SerializerException
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer
import com.google.common.base.Throwables
import com.google.common.io.ByteStreams
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.inject.AbstractModule
import com.google.inject.Provider
import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.reflect.Type
import java.time.Instant
import java.util.zip.GZIPInputStream

/**
 * @author Andrei Varabyeu
 */
class StackOverflowModule : AbstractModule() {

    override fun configure() {
        val gson = GsonBuilder()
                .registerTypeAdapter(Instant::class.java, com.google.gson.JsonDeserializer { json: JsonElement?, type: Type?, jsonDeserializationContext: JsonDeserializationContext? ->
                    Instant
                            .ofEpochMilli(json!!.asJsonPrimitive.asLong)
                })
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

        binder().bind(StackExchangeClient::class.java).toProvider(Provider {
            RestEndpoints.create()
                    .withBaseUrl(STACK_EXCHANGE_API_URL)
                    .withSerializer(object : GsonSerializer(gson) {
                        /* Dirty hack. RestEndpoint still does not support content decompression
                        and StackExchange does not support non-compressed responses  */
                        @Throws(SerializerException::class)
                        override fun <T> deserialize(content: ByteArray, type: Type): T {
                            try {
                                return super.deserialize<T>(
                                        ByteStreams.toByteArray(GZIPInputStream(ByteArrayInputStream(content))),
                                        type)
                            } catch (e: IOException) {
                                throw Throwables.propagate(e)
                            }

                        }
                    })
                    .forInterface(StackExchangeClient::class.java)
        })

    }

    companion object {
        val STACK_EXCHANGE_API_URL = "http://api.stackexchange.com/2.2"
    }
}

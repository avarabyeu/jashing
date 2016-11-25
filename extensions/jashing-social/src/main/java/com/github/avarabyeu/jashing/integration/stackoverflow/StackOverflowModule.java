package com.github.avarabyeu.jashing.integration.stackoverflow;

import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.http.exception.SerializerException;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.inject.AbstractModule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.zip.GZIPInputStream;

/**
 * @author Andrei Varabyeu
 */
public class StackOverflowModule extends AbstractModule {

    public static final String STACK_EXCHANGE_API_URL = "http://api.stackexchange.com/2.2";

    @Override
    protected void configure() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class,
                        (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext) -> Instant
                                .ofEpochMilli(json.getAsJsonPrimitive().getAsLong()))
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        binder().bind(StackExchangeClient.class).toProvider(() -> RestEndpoints.create()
                .withBaseUrl(STACK_EXCHANGE_API_URL)
                .withSerializer(new GsonSerializer(gson) {
                    /* Dirty hack. RestEndpoint still does not support content decompression
                        and StackExchange does not support non-compressed responses  */
                    @Override
                    public <T> T deserialize(byte[] content, Type type) throws SerializerException {
                        try {
                            return super.deserialize(
                                    ByteStreams.toByteArray(new GZIPInputStream(new ByteArrayInputStream(content))),
                                    type);
                        } catch (IOException e) {
                            throw Throwables.propagate(e);
                        }
                    }
                })
                .forInterface(StackExchangeClient.class));

    }
}

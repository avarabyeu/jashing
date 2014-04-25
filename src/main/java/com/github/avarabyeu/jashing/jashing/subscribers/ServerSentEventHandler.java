package com.github.avarabyeu.jashing.jashing.subscribers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.avarabyeu.jashing.jashing.controllers.ServerSentEvent;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import ninja.Context;
import ninja.Result;
import ninja.Results;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Server Sent Events handler.
 * Opens connection and keeps it opened waiting for sending commands/events.
 * Uses Guava's {@link com.google.common.eventbus.EventBus} for receiving events asynchronously, but leaves this stuff to subclasses
 *
 * @author avarabyeu
 * @see <a href="http://en.wikipedia.org/wiki/Server-sent_events">Server Sent Events</a>
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/EventBusExplained">Guava's EventBus</a>
 */
public abstract class ServerSentEventHandler<T> extends IndependentSubscriber<T> {

    /**
     * Initialized via constructor
     */
    private Context context;
    private ObjectMapper serializer;

    /**
     * Will be set via {@link #handle(ninja.Context)} method
     */
    private PrintWriter writer;


    @Inject
    public ServerSentEventHandler(EventBus eventBus, ObjectMapper serializer) {
        super(eventBus);
        this.serializer = Preconditions.checkNotNull(serializer, "Serializer shouldn't be null");
    }

    public Result handle(Context context) throws IOException {

        /* Obtains output writer from received request */
        Result result = Results.contentType("text/event-stream").addHeader("Cache-Control", "no-cache").addHeader("Connection", "keep-alive");
        result.charset(Charsets.UTF_8.displayName());
        this.context = context;
        this.writer = new PrintWriter(context.finalizeHeaders(result).getWriter());

        /* subscribes yourself to event bus */
        subscribe();

        /* returns async Ninja result to keep connection opened */
        return Results.async();
    }


    /**
     * Unsubscribes yourself from event bus and marks request as completed
     * to get to know Ninja that request is completed
     */
    @Override
    public void unsubscribe() {
        super.unsubscribe();
        context.asyncRequestComplete();
    }

    protected void writeEvent(ServerSentEvent event) {
         /* Lock is not necessary here once Guice guarantee that this method called synchronously */
        try {
            if (!Strings.isNullOrEmpty(event.getId())) {
                writer.write("id: ");
                writer.write(event.getId());
                writer.write("\n");
            }

            writer.write("data: ");
            writer.write(serializer.writeValueAsString(event.getData()));
            writer.write("\n\n");
            writer.flush();
            if (writer.checkError()) {
                unsubscribe();
            }
        } catch (JsonProcessingException e) {
            //TODO smth wrong with JSON mappings. Throw or not?
            e.printStackTrace();
        }
    }


}

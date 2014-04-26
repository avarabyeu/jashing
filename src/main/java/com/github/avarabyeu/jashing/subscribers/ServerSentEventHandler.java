package com.github.avarabyeu.jashing.subscribers;

import com.github.avarabyeu.jashing.ServerSentEvent;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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


    private Gson serializer;
    private Semaphore semaphore = new Semaphore(0);
    private Lock lock = new ReentrantLock();

    /**
     * Will be set via {@link #handle(spark.Request, spark.Response)} method
     */
    private PrintWriter writer;


    @Inject
    public ServerSentEventHandler(EventBus eventBus, Gson serializer) {
        super(eventBus);
        this.serializer = Preconditions.checkNotNull(serializer, "Serializer shouldn't be null");
    }

    public void handle(Request request, Response response) throws IOException {
        response.header("Cache-Control", "no-cache");
        response.header("Connection", "keep-alive");
        response.type("text/event-stream");
        //result.charset(Charsets.UTF_8.displayName());


        /* Obtains output writer from received request */
        this.writer = new PrintWriter(response.raw().getWriter());

        /* subscribes yourself to event bus */
        subscribe();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Unsubscribes yourself from event bus and marks request as completed
     * to get to know Ninja that request is completed
     */
    @Override
    public void unsubscribe() {
        super.unsubscribe();
        semaphore.release();
    }

    protected void writeEvent(ServerSentEvent event) {
         /* Lock is not necessary here once Guice guarantee that this method called synchronously */
        if (!Strings.isNullOrEmpty(event.getId())) {
            writer.write("id: ");
            writer.write(event.getId());
            writer.write("\n");
        }

        writer.write("data: ");
        writer.write(serializer.toJson(event.getData()));
        writer.write("\n\n");
        writer.flush();
        if (writer.checkError()) {
            unsubscribe();
        }
    }


}

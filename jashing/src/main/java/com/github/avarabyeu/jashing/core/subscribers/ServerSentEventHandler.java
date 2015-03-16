package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Monitor;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;

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


    private final Gson serializer;
    private final Semaphore requestLock = new Semaphore(0);

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
        response.type("text/event-stream;charset=UTF-8");


        /* Obtains output writer from received request */
        this.writer = new PrintWriter(response.raw().getWriter());

        /* subscribes yourself to event bus */
        subscribe();
        try {
            /* locks current request (keeps opened) until this handler subscribed to event bus */
            requestLock.acquire();
        } catch (InterruptedException e) {
            unsubscribe();
        }
    }


    /**
     * Unsubscribes yourself from event bus and marks and releases request lock
     */
    @Override
    public void unsubscribe() {
        super.unsubscribe();

        /* releases current request, because already unsubscribed from event bus. No need to keep request opened if there is no event appears */
        requestLock.release();
    }

    protected synchronized void writeEvent(ServerSentEvent event) {
         /* Actually, synchronization is not necessary here because Guava EventBus guarantee that this method called synchronously
          * Anyway, keep it synchronized to avoid misunderstanding
          */

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

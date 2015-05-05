package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletResponse;
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


    private final Gson serializer;

    /**
     * Will be set via {@link #handle(javax.servlet.AsyncContext)} method
     */
    private AsyncContext asyncContext;

    private PrintWriter writer;


    public ServerSentEventHandler(EventBus eventBus, Gson serializer) {
        super(eventBus);
        this.serializer = Preconditions.checkNotNull(serializer, "Serializer shouldn't be null");
    }

    public void handle(AsyncContext asyncContext) throws IOException {
        this.asyncContext = asyncContext;

        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.addHeader(HttpHeaders.CONNECTION, "keep-alive");
        response.setContentType("text/event-stream;charset=UTF-8");

        asyncContext.setTimeout(0);
        asyncContext.addListener(new EventBusSubscriptionListener());

        this.writer = response.getWriter();



        /* subscribes yourself to event bus */
        subscribe();
    }

    /* Actually, synchronization is not necessary here because Guava EventBus guarantee that this method called synchronously
     * Anyway, keep it synchronized to avoid misunderstanding
     */
    protected synchronized void writeEvent(ServerSentEvent event) {

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
            /* make sure request is closed in case of any error */
            asyncContext.complete();
        }

    }

    private class EventBusSubscriptionListener implements AsyncListener {

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            unsubscribe();
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
            unsubscribe();
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            unsubscribe();
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {

        }
    }


}

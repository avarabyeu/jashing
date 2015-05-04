package com.github.avarabyeu.jashing.core.subscribers;

import com.github.avarabyeu.jashing.core.ServerSentEvent;
import com.github.avarabyeu.jashing.core.ShutdownEvent;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Server Sent Events handler.
 * Opens connection and keeps it opened waiting for sending commands/events.
 * Uses Guava's {@link com.google.common.eventbus.EventBus} for receiving events asynchronously, but leaves this stuff to subclasses
 *
 * @author avarabyeu
 * @see <a href="http://en.wikipedia.org/wiki/Server-sent_events">Server Sent Events</a>
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/EventBusExplained">Guava's EventBus</a>
 */
@NotThreadSafe
public class ServerSentEventHandlerImpl implements ServerSentEventsHandler, WriteListener {

    private final EventBus eventBus;
    private final Gson serializer;

    private AsyncContext async;
    private ServletOutputStream stream;
    private Queue<String> queue = new LinkedList<>();

    @Inject
    public ServerSentEventHandlerImpl(EventBus eventBus, Gson serializer) {
        this.eventBus = eventBus;
        this.serializer = Preconditions.checkNotNull(serializer, "Serializer shouldn't be null");
    }

    public void handle(AsyncContext asyncContext) throws IOException {
        this.async = asyncContext;

        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.addHeader(HttpHeaders.CONNECTION, "keep-alive");
        response.setContentType("text/event-stream;charset=UTF-8");
        response.getOutputStream().setWriteListener(this);
        asyncContext.setTimeout(0);

        this.eventBus.register(this);

    }


    /* Actually, synchronization is not necessary here because Guava EventBus guarantee that this method called synchronously
     * Anyway, keep it synchronized to avoid misunderstanding
    */
    @Override
    public void onEvent(ServerSentEvent event) throws IOException {
        System.out.println("dispatched");
        StringBuilder content = new StringBuilder();

        if (!Strings.isNullOrEmpty(event.getId())) {
            content.append("id: ")
                    .append(event.getId())
                    .append("\n");
        }
        content.append("data: ")
                .append(serializer.toJson(event.getData()))
                .append("\n\n");

        boolean isSent = false;
        while (async.getResponse().getOutputStream().isReady() && !isSent) {
            async.getResponse().getOutputStream().print(content.toString());
            isSent = true;
            //async.getResponse().flushBuffer();
            System.out.println("printed");
        }

    }


    /**
     * Internal shutdown e.g. application stop
     *
     * @param shutdownEvent Event
     */
    @Override
    public void onShutdown(ShutdownEvent shutdownEvent) {
        this.shutdown();
    }


    /**
     * Servlet API listener method. Once write possible, we can register can be registered in event bus
     *
     * @throws IOException
     */
    @Override
    public void onWritePossible() throws IOException {
//        stream = async.getResponse().getOutputStream();
//        this.eventBus.register(this);
        while(async.getResponse().getOutputStream().isReady()){
            async.getResponse().getOutputStream().flush();
        }
    }

    /**
     * Servlet API listener method. We should unregister from event bus
     *
     * @param t Error
     */
    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        shutdown();
    }

    private void shutdown() {
        this.eventBus.unregister(this);
        this.async.complete();
    }


}

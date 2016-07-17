package com.github.avarabyeu.jashing.core

import com.github.avarabyeu.jashing.utils.StringUtils.substringBefore
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.google.common.util.concurrent.AbstractIdleService
import com.google.common.util.concurrent.RateLimiter
import com.google.gson.Gson
import freemarker.cache.ClassTemplateLoader
import io.undertow.Handlers
import io.undertow.Undertow
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.resource.ClassPathResourceManager
import io.undertow.server.handlers.sse.ServerSentEventHandler
import io.undertow.servlet.Servlets
import io.undertow.servlet.util.ImmediateInstanceHandle
import io.undertow.util.AttachmentKey
import io.undertow.util.Headers
import io.undertow.util.StatusCodes
import ro.isdc.wro.http.ConfigurableWroFilter
import java.io.StringWriter
import java.util.*
import java.util.function.Consumer
import javax.servlet.DispatcherType
import javax.servlet.ServletException

/**
 * HTTP Server Controller. Bootstraps server and specifies all needed mappings and request handlers

 * @author avarabyeu
 */
internal class JashingServer(private val port: Int, private val eventBus: EventBus, private val gson: Gson, private val timeout: Long?) : AbstractIdleService() {

    private lateinit var sseHandler: ServerSentEventHandler
    private lateinit var server: Undertow

    @Throws(Exception::class)
    override fun startUp() {
        val configuration = freemarker.template.Configuration(
                freemarker.template.Configuration.VERSION_2_3_23)
        configuration.templateLoader = ClassTemplateLoader(Jashing::class.java, "/")

        sseHandler = Handlers.serverSentEvents { connection, lastEventId ->
            timeout?.let { connection.putAttachment(RATE_LIMITER_KEY, RateLimiter.create(it.toDouble())) }
        }

        eventBus.register(Subscriber {
            serverSentEvent ->
            sseHandler.connections.forEach { c ->
                //wait if there is rate limiter
                c.getAttachment(RATE_LIMITER_KEY)?.let { it.acquire() }

                c.send(gson.toJson(serverSentEvent))
            }
        })

        val widgetsHandler = Handlers.resource(
                ClassPathResourceManager(Thread.currentThread().contextClassLoader,
                        "statics"))

        val routingHandler = Handlers.routing()["/views/{widget}", { exchange ->

            exchange.statusCode = StatusCodes.FOUND
            val widgetName = exchange.queryParameters["widget"]!!.first
            exchange.responseHeaders.put(Headers.LOCATION,
                    "/assets/widgets/" + substringBefore(widgetName, ".html") + "/" + widgetName)
            exchange.endExchange()

        }]["/{dashboard}", { exchange ->
            val out = StringWriter()
            configuration.getTemplate(
                    "/views/dashboards/" + exchange.queryParameters["dashboard"]!!.poll()
                            + ".ftl.html").process(Collections.EMPTY_MAP, out)

            exchange.responseSender.send(out.toString())
        }].get("/events", sseHandler).get("/", Handlers.redirect("/sample"))

        val rootHandler = Handlers.path(routingHandler).addPrefixPath("/assets", widgetsHandler).addPrefixPath("/compiled", aggregationHandler)

        server = Undertow.builder().addHttpListener(port, "0.0.0.0").setHandler(rootHandler).build()

        server.start()
    }

    /**
     * Uses Wro4j Filter to pre-process resources
     * Required for coffee scripts compilation and saas processing
     * Wro4j uses Servlet API so we make fake Servlet Deployment here to emulate servlet-based environment

     * @return Static resources handler
     */
    private val aggregationHandler: HttpHandler
        @Throws(ServletException::class)
        get() {
            val deploymentInfo = Servlets.deployment().setClassLoader(JashingServer::class.java.classLoader).setContextPath("/").setDeploymentName("jashing").addFilterUrlMapping("wro4j", "/*", DispatcherType.REQUEST).addFilter(Servlets.filter("wro4j", ConfigurableWroFilter::class.java
            ) {
                val filter = ConfigurableWroFilter()
                filter.wroManagerFactory = WroManagerFactory()
                ImmediateInstanceHandle(filter)
            })
            val deployment = Servlets.defaultContainer().addDeployment(deploymentInfo)
            deployment.deploy()
            return deployment.start()
        }

    @Throws(Exception::class)
    override fun shutDown() {
        /* close connections */
        sseHandler.connections.forEach { it.shutdown() }

        /* stop the server */
        server.stop()

    }

    /**
     * We need Guava's Subscribe annotation to be placed on callback function.
     * This is why we just wrap callback with this class
     */
    class Subscriber(val callback: (event: JashingEvent) -> Unit) : Consumer<JashingEvent> {

        @Subscribe
        override fun accept(event: JashingEvent?) {
            event?.let { callback.invoke(it) }
        }

    }

    companion object {
        val RATE_LIMITER_KEY = AttachmentKey.create(RateLimiter::class.java)

    }
}

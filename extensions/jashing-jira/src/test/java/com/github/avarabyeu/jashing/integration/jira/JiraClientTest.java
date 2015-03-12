package com.github.avarabyeu.jashing.integration.jira;


import com.github.avarabyeu.jashing.integration.jira.model.BasicIssue;
import com.github.avarabyeu.jashing.integration.jira.model.SearchResult;
import com.github.avarabyeu.jashing.utils.ResourceUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Names;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;

/**
 * Some Unit tests for {@link com.github.avarabyeu.jashing.integration.jira.JiraClient}
 *
 * @author Andrei Varabyeu
 */
public class JiraClientTest {

    public static final String MOCKED_RESPONSE_JSON = "mocked-response.json";

    private static MockWebServer server;

    private static JiraClient jiraClient;

    @BeforeClass
    public static void startServer() throws IOException {
        server = new MockWebServer();
        server.play();

        jiraClient = Guice.createInjector(new JiraModule(), new AbstractModule() {
            @Override
            protected void configure() {
                Map<String, String> properties = new HashMap<>();
                properties.put("jira.url", "http://localhost:" + server.getPort());
                properties.put("jira.user.login", "");
                properties.put("jira.user.password", "");
                Names.bindProperties(binder(), properties);
            }
        }).getInstance(JiraClient.class);
    }

    @AfterClass
    public static void shutdownServer() throws IOException {
        server.shutdown();
    }

    @Test
    public void testJiraClient() throws InterruptedException {
        server.enqueue(new MockResponse().setBody(ResourceUtils.getResourceAsString(MOCKED_RESPONSE_JSON)));


        /* Check response parsing */
        SearchResult searchResult = jiraClient.findTickets("issuekey=DEMO");
        Assert.assertThat(searchResult, notNullValue());
        Assert.assertThat(searchResult.getIssues(), not(empty()));
        Assert.assertThat(searchResult.getTotal(), is(1));

        BasicIssue foundIssue = searchResult.getIssues().get(0);
        Assert.assertThat(foundIssue.getKey(), is("DEMO"));
        Assert.assertThat(foundIssue.getSelf(), is(URI.create("http://localhost/rest/api/2/issue/1")));

        /* Check request to server */
        Assert.assertThat(server.takeRequest().getPath(), is("/rest/api/2/search?jql=issuekey=DEMO&fields=id,key"));

    }
}

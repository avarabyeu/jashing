package com.github.avarabyeu.jashing.integration.jira;

import com.github.avarabyeu.jashing.core.EventSource;
import com.github.avarabyeu.jashing.core.eventsource.ScheduledEventSource;
import com.github.avarabyeu.jashing.events.ListEvent;

/**
 * @author Andrei Varabyeu
 */
@EventSource(value = "jira", explicitConfiguration = JiraModule.class)
public class QueryResultEventSource extends ScheduledEventSource<ListEvent<String>> {

    //@Inject
    //private JiraClient jiraClient;

//    @Inject
//    @Named("jql")
    //private String jql;

    @Override
    protected ListEvent<String> produceEvent() {

        System.out.println("I'm working!!!");

//        AsynchronousJiraRestClientFactory f = new AsynchronousJiraRestClientFactory();
//        JiraRestClient jc = f.createWithBasicHttpAuthentication(new URI("http://localhost:8080"), "admin", "123");
//
//        com.atlassian.util.concurrent.Promise<SearchResult> searchResultPromise = (com.atlassian.util.concurrent.Promise<SearchResult>) jc.getSearchClient().searchJql("type = Epic ORDER BY RANK ASC");


        return null;
    }
}

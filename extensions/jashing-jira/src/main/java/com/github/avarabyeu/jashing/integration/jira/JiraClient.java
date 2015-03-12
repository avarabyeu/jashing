package com.github.avarabyeu.jashing.integration.jira;

import com.github.avarabyeu.jashing.integration.jira.model.SearchResult;
import com.github.avarabyeu.restendpoint.http.HttpMethod;
import com.github.avarabyeu.restendpoint.http.annotation.Path;
import com.github.avarabyeu.restendpoint.http.annotation.Request;

/**
 * Simple JIRA client
 *
 * @author Andrei Varabyeu
 */
public interface JiraClient {

    @Request(method = HttpMethod.GET, url = "/rest/api/2/search?jql={jql}&fields=id,key")
    SearchResult findTickets(@Path("jql") String jql);
}

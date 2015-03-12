package com.github.avarabyeu.jashing.integration.jira;

import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

/**
 * @author Andrei Varabyeu
 */
public class JiraModule extends AbstractModule {


    @Provides
    public JiraClient provideJiraClient(@Named("jira.url") String jiraBaseUrl,
                                        @Named("jira.user.login") String jiraUserName,
                                        @Named("jira.user.password") String jiraPassword) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(jiraBaseUrl), "Jira Base URL is null or empty. Please, check your configuration");
        return RestEndpoints.create()
                .withBaseUrl(jiraBaseUrl)
                .withBasicAuth(jiraUserName, jiraPassword)
                .withSerializer(new GsonSerializer())
                .forInterface(JiraClient.class);
    }


    @Override
    protected void configure() {

    }
}

package com.github.avarabyeu.jashing.integration.jenkins;

import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.http.exception.SerializerException;
import com.github.avarabyeu.restendpoint.serializer.StringSerializer;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.github.avarabyeu.restendpoint.serializer.xml.JaxbSerializer;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;

/**
 * Configures jenkins client
 *
 * @author Andrei Varabyeu
 */
public class JenkinsModule extends AbstractModule {

    @Override
    protected void configure() {
        //configure with provides methods
    }

    @Provides
    @Inject(optional = true)
    public JenkinsClient provideJenkinsClient(@Named("jenkins.url") String jenkinsUrl) throws SerializerException {
        if (Strings.isNullOrEmpty(jenkinsUrl)) {
            binder().addError("Jenkins URL is not specified. Please, check 'jenkins.url' parameter in configuration");
        }
        return RestEndpoints.create().withBaseUrl(jenkinsUrl)
                .withSerializer(new JaxbSerializer(Jobs.class)).withSerializer(new GsonSerializer()).withSerializer(new StringSerializer())
                .forInterface(JenkinsClient.class);
    }

}

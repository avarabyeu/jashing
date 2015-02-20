package com.github.avarabyeu.jashing.eventsource;

import com.github.avarabyeu.jashing.integration.jenkins.JenkinsClient;
import com.github.avarabyeu.jashing.integration.jenkins.Jobs;
import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.http.exception.SerializerException;
import com.github.avarabyeu.restendpoint.serializer.StringSerializer;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.github.avarabyeu.restendpoint.serializer.xml.JaxbSerializer;
import com.google.common.net.UrlEscapers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Andrey Vorobyov
 */
public class JenkinsTest {

    @Test
    @Ignore
    public void testClient() throws SerializerException {
        JenkinsClient jenkinsClient = RestEndpoints.create().withBaseUrl("http://jenkins.cte-minsk.local:8080/")
                .withSerializer(new JaxbSerializer(Jobs.class)).withSerializer(new GsonSerializer()).withSerializer(new StringSerializer())
                .forInterface(JenkinsClient.class);
        System.out.println(jenkinsClient.getRunningJobs());
        //System.out.println(jenkinsClient.getJobProgress("DFGFG"));
        for (String job : jenkinsClient.getRunningJobs().getNames()){
            System.out.println(jenkinsClient.getJobProgress(job));
        }

    }
}

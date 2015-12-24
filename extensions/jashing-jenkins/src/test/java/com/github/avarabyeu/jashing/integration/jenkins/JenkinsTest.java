package com.github.avarabyeu.jashing.integration.jenkins;

import com.github.avarabyeu.restendpoint.http.RestEndpoints;
import com.github.avarabyeu.restendpoint.http.exception.SerializerException;
import com.github.avarabyeu.restendpoint.serializer.Serializer;
import com.github.avarabyeu.restendpoint.serializer.StringSerializer;
import com.github.avarabyeu.restendpoint.serializer.TextSerializer;
import com.github.avarabyeu.restendpoint.serializer.json.GsonSerializer;
import com.github.avarabyeu.restendpoint.serializer.xml.JaxbSerializer;
import com.google.common.base.Charsets;
import com.google.common.net.MediaType;
import com.google.common.reflect.TypeToken;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author Andrey Vorobyov
 */
public class JenkinsTest {

    @Test
    @Ignore
    public void testClient() throws SerializerException, ExecutionException, InterruptedException {
        JenkinsClient jenkinsClient = RestEndpoints.create().withBaseUrl("http://jenkins.cte-minsk.local:8080/")
                .withSerializer(new JaxbSerializer(Jobs.class)).withSerializer(new GsonSerializer()).withSerializer(new TextSerializer())
                .forInterface(JenkinsClient.class);
        System.out.println(jenkinsClient.getRunningJobs());
        //System.out.println(jenkinsClient.getJobProgress("DFGFG"));
        System.out.println(jenkinsClient.getJobProgress("225 antifraud").get());
        for (String job : jenkinsClient.getRunningJobs().getNames()) {
            System.out.println(job);
            System.out.println(jenkinsClient.getJobProgress(job));
        }

    }
}

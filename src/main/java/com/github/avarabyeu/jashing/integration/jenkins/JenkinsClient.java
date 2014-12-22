package com.github.avarabyeu.jashing.integration.jenkins;


import com.github.avarabyeu.restendpoint.http.HttpMethod;
import com.github.avarabyeu.restendpoint.http.annotation.Path;
import com.github.avarabyeu.restendpoint.http.annotation.Request;

/**
 * Simple Jenkins client
 *
 * @author Andrey Vorobyov
 */
public interface JenkinsClient {

    @Request(method = HttpMethod.GET, url = "api/xml?tree=jobs[name,url,color]&xpath=/hudson/job[ends-with(color/text(),%22_anime%22)]/name&wrapper=names")
    Jobs getRunningJobs();

    @Request(method = HttpMethod.GET, url = "job/{jobName}/lastBuild/api/xml?depth=1&xpath=*/executor/progress/text()")
    String getJobProgress(@Path("jobName") String jobName);

}

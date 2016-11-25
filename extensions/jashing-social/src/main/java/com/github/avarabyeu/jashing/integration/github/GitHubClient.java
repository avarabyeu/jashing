package com.github.avarabyeu.jashing.integration.github;

import com.github.avarabyeu.jashing.integration.github.model.Issue;
import com.github.avarabyeu.jashing.integration.github.model.PullRequest;
import com.github.avarabyeu.jashing.integration.github.model.Repository;
import com.github.avarabyeu.restendpoint.http.HttpMethod;
import com.github.avarabyeu.restendpoint.http.annotation.Path;
import com.github.avarabyeu.restendpoint.http.annotation.Request;

import java.util.List;

/**
 * GitHub client
 *
 * @author Andrei Varabyeu
 */
public interface GitHubClient {

    /**
     * Obtains list of repositories for logged in user
     *
     * @return Repositories list
     */
    @Request(method = HttpMethod.GET, url = "/user/repos")
    List<Repository> getUserRepositories();

    /**
     * Obtains list of repositories for provided organization
     *
     * @param org Username
     * @return Repositories list
     */
    @Request(method = HttpMethod.GET, url = "/orgs/{org}/repos")
    List<Repository> getOrganizationRepositories(@Path("org") String org);

    /**
     * Obtains list of repositories for provided user
     *
     * @param username Username
     * @return Repositories list
     */
    @Request(method = HttpMethod.GET, url = "/users/{username}/repos")
    List<Repository> getUserRepositories(@Path("username") String username);

    /**
     * Obtains repository for provided user
     *
     * @param owner      Repository owner
     * @param repository Repository name
     * @return Repository
     */
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}")
    Repository getUserRepository(@Path("owner") String owner, @Path("repo") String repository);

    /**
     * Obtains pull requests for provided user and repository
     *
     * @param owner      Repository owner
     * @param repository Repository name
     * @return Pull Requests list
     */
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}/pulls?state=open")
    List<PullRequest> getOpenedPullRequests(@Path("owner") String owner, @Path("repo") String repository);

    /**
     * Obtains pull request by it's number
     *
     * @param owner      Repository owner
     * @param repository Repository name
     * @param id         PR's ID
     * @return Pull Requests list
     */
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}/pulls/{number}")
    PullRequest getPullRequest(@Path("owner") String owner, @Path("repo") String repository, @Path("number") String id);

    /**
     * Obtains pull requests for provided user and repository
     *
     * @param organization Repository owner
     * @return List of pull requests
     */
    @Request(method = HttpMethod.GET, url = "/orgs/{org}/issues?filter=all&state=opened")
    List<Issue> getOpenedIssues(@Path("org") String organization);

}

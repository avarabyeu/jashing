package com.github.avarabyeu.jashing.integration.github

import com.github.avarabyeu.restendpoint.http.HttpMethod
import com.github.avarabyeu.restendpoint.http.annotation.Path
import com.github.avarabyeu.restendpoint.http.annotation.Request

interface GitHubClient {

    /**
     * Obtains list of repositories for logged in user
     *
     * @return Repositories list
     */
    @Request(method = HttpMethod.GET, url = "/user/repos")
    fun getUserRepositories(): List<Repository>

    /**
     * Obtains list of repositories for provided organization
     *
     * @param org Username
     * @return Repositories list
     */
    @Request(method = HttpMethod.GET, url = "/orgs/{org}/repos")
    fun getOrganizationRepositories(@Path("org") org: String): List<Repository>

    /**
     * Obtains list of repositories for provided user
     *
     * @param username Username
     * @return Repositories list
     */
    @Request(method = HttpMethod.GET, url = "/users/{username}/repos")
    fun getUserRepositories(@Path("username") username: String): List<Repository>

    /**
     * Obtains repository for provided user
     *
     * @param owner      Repository owner
     * @param repository Repository name
     * @return Repository
     */
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}")
    fun getUserRepository(@Path("owner") owner: String, @Path("repo") repository: String): Repository

    /**
     * Obtains pull requests for provided user and repository
     *
     * @param owner      Repository owner
     * @param repository Repository name
     * @return Pull Requests list
     */
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}/pulls?state=open")
    fun getOpenedPullRequests(@Path("owner") owner: String, @Path("repo") repository: String): List<PullRequest>

    /**
     * Obtains pull request by it's number
     *
     * @param owner      Repository owner
     * @param repository Repository name
     * @param id         PR's ID
     * @return Pull Requests list
     */
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}/pulls/{number}")
    fun getPullRequest(@Path("owner") owner: String, @Path("repo") repository: String, @Path("number") id: String): PullRequest

    /**
     * Obtains pull requests for provided user and repository
     *
     * @param organization Repository owner
     * @return List of pull requests
     */
    @Request(method = HttpMethod.GET, url = "/orgs/{org}/issues?filter=all&state=opened")
    fun getOpenedIssues(@Path("org") organization: String): List<Issue>

}

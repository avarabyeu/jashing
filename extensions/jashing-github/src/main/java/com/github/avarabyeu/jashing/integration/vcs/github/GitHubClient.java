package com.github.avarabyeu.jashing.integration.vcs.github;

import com.github.avarabyeu.jashing.integration.vcs.github.model.PullRequest;
import com.github.avarabyeu.jashing.integration.vcs.github.model.Repository;
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
    @Request(method = HttpMethod.GET, url = "/repos/{owner}/{repo}/pulls")
    List<PullRequest> getPullRequests(@Path("owner") String owner, @Path("repo") String repository);

}

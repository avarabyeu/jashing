
/**
 * ***************************************************************************
 * Copyright (c) 2011 GitHub Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Kevin Sawicki (GitHub Inc.) - initial API and implementation
 * ***************************************************************************
 */
package com.github.avarabyeu.jashing.integration.vcs.github.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Repository model class
 */
public class Repository {

    private boolean fork;

    private boolean hasDownloads;

    private boolean hasIssues;

    private boolean hasWiki;

    @SerializedName("private")
    private boolean isPrivate;

    private Date createdAt;

    private Date pushedAt;

    private Date updatedAt;

    private int forks;

    private long id;

    @SerializedName("open_issues_count")
    private int openIssues;

    private int size;

    private int watchers;

    private Repository parent;

    private Repository source;

    private String cloneUrl;

    private String description;

    private String homepage;

    private String gitUrl;

    private String htmlUrl;

    private String language;

    private String defaultBranch;

    private String mirrorUrl;

    private String name;

    private String sshUrl;

    private String svnUrl;

    private String url;

    private User owner;

    /**
     * @return fork
     */
    public boolean isFork() {
        return fork;
    }

    /**
     * @param fork Fork
     * @return this repository
     */
    public Repository setFork(boolean fork) {
        this.fork = fork;
        return this;
    }

    /**
     * @return hasDownloads
     */
    public boolean isHasDownloads() {
        return hasDownloads;
    }

    /**
     * @param hasDownloads Has Downloads
     * @return this repository
     */
    public Repository setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
        return this;
    }

    /**
     * @return hasIssues
     */
    public boolean isHasIssues() {
        return hasIssues;
    }

    /**
     * @param hasIssues Has Issues
     * @return this repository
     */
    public Repository setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
        return this;
    }

    /**
     * @return hasWiki
     */
    public boolean isHasWiki() {
        return hasWiki;
    }

    /**
     * @param hasWiki Has Wiki
     * @return this repository
     */
    public Repository setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
        return this;
    }

    /**
     * @return isPrivate
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * @param isPrivate Is Private
     * @return this repository
     */
    public Repository setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    /**
     * @return createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Created At
     * @return this rdateepository
     */
    public Repository setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @return pushedAt
     */
    public Date getPushedAt() {
        return pushedAt;
    }

    /**
     * @param pushedAt Pushed At
     * @return this repository
     */
    public Repository setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
        return this;
    }

    /**
     * @return forks
     */
    public int getForks() {
        return forks;
    }

    /**
     * @param forks Forks
     * @return this repository
     */
    public Repository setForks(int forks) {
        this.forks = forks;
        return this;
    }

    /**
     * @return openIssues
     */
    public int getOpenIssues() {
        return openIssues;
    }

    /**
     * @param openIssues Open Issues
     * @return this repository
     */
    public Repository setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
        return this;
    }

    /**
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size Size
     * @return this repository
     */
    public Repository setSize(int size) {
        this.size = size;
        return this;
    }

    /**
     * @return watchers
     */
    public int getWatchers() {
        return watchers;
    }

    /**
     * @param watchers Watchers
     * @return this repository
     */
    public Repository setWatchers(int watchers) {
        this.watchers = watchers;
        return this;
    }

    /**
     * @return parent
     */
    public Repository getParent() {
        return parent;
    }

    /**
     * @param parent Parent
     * @return this repository
     */
    public Repository setParent(Repository parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @return source
     */
    public Repository getSource() {
        return source;
    }

    /**
     * @param source Source
     * @return this repository
     */
    public Repository setSource(Repository source) {
        this.source = source;
        return this;
    }

    /**
     * @return cloneUrl
     */
    public String getCloneUrl() {
        return cloneUrl;
    }

    /**
     * @param cloneUrl Clone URL
     * @return this repository
     */
    public Repository setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
        return this;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Description
     * @return this repository
     */
    public Repository setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @return homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage Homepage
     * @return this repository
     */
    public Repository setHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    /**
     * @return gitUrl
     */
    public String getGitUrl() {
        return gitUrl;
    }

    /**
     * @param gitUrl Get URL
     * @return this repository
     */
    public Repository setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
        return this;
    }

    /**
     * @return htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl HTML URL
     * @return this repository
     */
    public Repository setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
        return this;
    }

    /**
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language Language
     * @return this repository
     */
    public Repository setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * @return defaultBranch
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * @param defaultBranch Default Branch
     * @return this repository
     */
    public Repository setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
        return this;
    }

    /**
     * @return masterBranch
     */
    @Deprecated
    public String getMasterBranch() {
        return defaultBranch;
    }

    /**
     * @param masterBranch Master Branch
     * @return this repository
     */
    @Deprecated
    public Repository setMasterBranch(String masterBranch) {
        this.defaultBranch = masterBranch;
        return this;
    }

    /**
     * @return mirrorUrl
     */
    public String getMirrorUrl() {
        return mirrorUrl;
    }

    /**
     * @param mirrorUrl Mirror URL
     * @return this repository
     */
    public Repository setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
        return this;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Name
     * @return this repository
     */
    public Repository setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return sshUrl
     */
    public String getSshUrl() {
        return sshUrl;
    }

    /**
     * @param sshUrl SSH URL
     * @return this repository
     */
    public Repository setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
        return this;
    }

    /**
     * @return svnUrl
     */
    public String getSvnUrl() {
        return svnUrl;
    }

    /**
     * @param svnUrl SVN URL
     * @return this repository
     */
    public Repository setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
        return this;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url URL
     * @return this repository
     */
    public Repository setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner Owner
     * @return this repository
     */
    public Repository setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    /**
     * @return updatedAt
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt Updated At
     * @return this repository
     */
    public Repository setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id ID
     * @return this repository
     */
    public Repository setId(long id) {
        this.id = id;
        return this;
    }

    /**
     * Generate id for this repository
     * @return New generated ID
     */
    public String generateId() {
        final User owner = this.owner;
        final String name = this.name;
        if (owner == null || name == null || name.length() == 0)
            return null;
        final String login = owner.getLogin();
        if (login == null || login.length() == 0)
            return null;
        return login + "/" + name; //$NON-NLS-1$
    }

    @Override
    public String toString() {
        return "Repository{" +
                "fork=" + fork +
                ", hasDownloads=" + hasDownloads +
                ", hasIssues=" + hasIssues +
                ", hasWiki=" + hasWiki +
                ", isPrivate=" + isPrivate +
                ", createdAt=" + createdAt +
                ", pushedAt=" + pushedAt +
                ", updatedAt=" + updatedAt +
                ", forks=" + forks +
                ", id=" + id +
                ", openIssues=" + openIssues +
                ", size=" + size +
                ", watchers=" + watchers +
                ", parent=" + parent +
                ", source=" + source +
                ", cloneUrl='" + cloneUrl + '\'' +
                ", description='" + description + '\'' +
                ", homepage='" + homepage + '\'' +
                ", gitUrl='" + gitUrl + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", language='" + language + '\'' +
                ", defaultBranch='" + defaultBranch + '\'' +
                ", mirrorUrl='" + mirrorUrl + '\'' +
                ", name='" + name + '\'' +
                ", sshUrl='" + sshUrl + '\'' +
                ", svnUrl='" + svnUrl + '\'' +
                ", url='" + url + '\'' +
                ", owner=" + owner +
                '}';
    }
}

/*******************************************************************************
 * Copyright (c) 2011 GitHub Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *******************************************************************************/
package com.github.avarabyeu.jashing.integration.github.model;

import java.util.Date;

/**
 * GitHub user model class.
 * @author Andrei Varabyeu
 */
public class User {

    /**
     * TYPE_USER
     */
    public static final String TYPE_USER = "User"; //$NON-NLS-1$

    /**
     * TYPE_ORG
     */
    public static final String TYPE_ORG = "Organization"; //$NON-NLS-1$

    private boolean hireable;

    private Date createdAt;

    private int collaborators;

    private int diskUsage;

    private int followers;

    private int following;

    private int id;

    private int ownedPrivateRepos;

    private int privateGists;

    private int publicGists;

    private int publicRepos;

    private int totalPrivateRepos;

    private String avatarUrl;

    private String bio;

    private String blog;

    private String company;

    private String email;

    private String gravatarId;

    private String htmlUrl;

    private String location;

    private String login;

    private String name;

    private String type;

    private String url;

    private UserPlan plan;

    /**
     * @return hireable
     */
    public boolean isHireable() {
        return hireable;
    }

    /**
     * @param hireable Hireable
     * @return this user
     */
    public User setHireable(boolean hireable) {
        this.hireable = hireable;
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
     * @return this user
     */
    public User setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @return collaborators
     */
    public int getCollaborators() {
        return collaborators;
    }

    /**
     * @param collaborators Collaborators
     * @return this user
     */
    public User setCollaborators(int collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    /**
     * @return diskUsage
     */
    public int getDiskUsage() {
        return diskUsage;
    }

    /**
     * @param diskUsage Disk Usage
     * @return this user
     */
    public User setDiskUsage(int diskUsage) {
        this.diskUsage = diskUsage;
        return this;
    }

    /**
     * @return followers
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * @param followers Followers
     * @return this user
     */
    public User setFollowers(int followers) {
        this.followers = followers;
        return this;
    }

    /**
     * @return following
     */
    public int getFollowing() {
        return following;
    }

    /**
     * @param following Following
     * @return this user
     */
    public User setFollowing(int following) {
        this.following = following;
        return this;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id ID
     * @return this user
     */
    public User setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * @return ownedPrivateRepos
     */
    public int getOwnedPrivateRepos() {
        return ownedPrivateRepos;
    }

    /**
     * @param ownedPrivateRepos Owned Private Repos
     * @return this user
     */
    public User setOwnedPrivateRepos(int ownedPrivateRepos) {
        this.ownedPrivateRepos = ownedPrivateRepos;
        return this;
    }

    /**
     * @return privateGists
     */
    public int getPrivateGists() {
        return privateGists;
    }

    /**
     * @param privateGists Private Gists
     * @return this user
     */
    public User setPrivateGists(int privateGists) {
        this.privateGists = privateGists;
        return this;
    }

    /**
     * @return publicGists
     */
    public int getPublicGists() {
        return publicGists;
    }

    /**
     * @param publicGists Public Gists
     * @return this user
     */
    public User setPublicGists(int publicGists) {
        this.publicGists = publicGists;
        return this;
    }

    /**
     * @return publicRepos
     */
    public int getPublicRepos() {
        return publicRepos;
    }

    /**
     * @param publicRepos Public Repos
     * @return this user
     */
    public User setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
        return this;
    }

    /**
     * @return totalPrivateRepos
     */
    public int getTotalPrivateRepos() {
        return totalPrivateRepos;
    }

    /**
     * @param totalPrivateRepos Total Private Repos count
     * @return this user
     */
    public User setTotalPrivateRepos(int totalPrivateRepos) {
        this.totalPrivateRepos = totalPrivateRepos;
        return this;
    }

    /**
     * @return avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * @param avatarUrl Avatar URL
     * @return this user
     */
    public User setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    /**
     * @return bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * @param bio Biography
     * @return this user
     */
    public User setBio(String bio) {
        this.bio = bio;
        return this;
    }

    /**
     * @return blog
     */
    public String getBlog() {
        return blog;
    }

    /**
     * @param blog Blog
     * @return this user
     */
    public User setBlog(String blog) {
        this.blog = blog;
        return this;
    }

    /**
     * @return company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company Company
     * @return this user
     */
    public User setCompany(String company) {
        this.company = company;
        return this;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email Email
     * @return this user
     */
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * @return gravatarId
     * @deprecated
     */
    @Deprecated
    public String getGravatarId() {
        return gravatarId;
    }

    /**
     * @param gravatarId Avatar ID
     * @return this user
     * @deprecated
     */
    @Deprecated
    public User setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
        return this;
    }

    /**
     * @return htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl Html URL
     * @return this user
     */
    public User setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
        return this;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location Location
     * @return this user
     */
    public User setLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login Login
     * @return this user
     */
    public User setLogin(String login) {
        this.login = login;
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
     * @return this user
     */
    public User setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Type
     * @return this user
     */
    public User setType(String type) {
        this.type = type;
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
     * @return this user
     */
    public User setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return plan
     */
    public UserPlan getPlan() {
        return plan;
    }

    /**
     * @param plan Plan
     * @return this user
     */
    public User setPlan(UserPlan plan) {
        this.plan = plan;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "hireable=" + hireable +
                ", createdAt=" + createdAt +
                ", collaborators=" + collaborators +
                ", diskUsage=" + diskUsage +
                ", followers=" + followers +
                ", following=" + following +
                ", id=" + id +
                ", ownedPrivateRepos=" + ownedPrivateRepos +
                ", privateGists=" + privateGists +
                ", publicGists=" + publicGists +
                ", publicRepos=" + publicRepos +
                ", totalPrivateRepos=" + totalPrivateRepos +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", bio='" + bio + '\'' +
                ", blog='" + blog + '\'' +
                ", company='" + company + '\'' +
                ", email='" + email + '\'' +
                ", gravatarId='" + gravatarId + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", location='" + location + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", plan=" + plan +
                '}';
    }
}

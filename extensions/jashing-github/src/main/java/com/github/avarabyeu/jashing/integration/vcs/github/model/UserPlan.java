/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/

package com.github.avarabyeu.jashing.integration.vcs.github.model;

/**
 * User plan model class.
 */
public class UserPlan {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4759542049129654659L;

    private long collaborators;

    private long privateRepos;

    private long space;

    private String name;

    /**
     * @return collaborators
     */
    public long getCollaborators() {
        return collaborators;
    }

    /**
     * @param collaborators Collaborators
     * @return this user plan
     */
    public UserPlan setCollaborators(long collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    /**
     * @return privateRepos
     */
    public long getPrivateRepos() {
        return privateRepos;
    }

    /**
     * @param privateRepos Private repos
     * @return this user plan
     */
    public UserPlan setPrivateRepos(long privateRepos) {
        this.privateRepos = privateRepos;
        return this;
    }

    /**
     * @return space
     */
    public long getSpace() {
        return space;
    }

    /**
     * @param space Space
     * @return this user plan
     */
    public UserPlan setSpace(long space) {
        this.space = space;
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
     * @return this user plan
     */
    public UserPlan setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "UserPlan{" +
                "collaborators=" + collaborators +
                ", privateRepos=" + privateRepos +
                ", space=" + space +
                ", name='" + name + '\'' +
                '}';
    }
}

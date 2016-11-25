/**
 * ****************************************************************************
 * Copyright (c) 2011 GitHub Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * Kevin Sawicki (GitHub Inc.) - initial API and implementation
 * *****************************************************************************
 */

package com.github.avarabyeu.jashing.integration.github.model;

import java.util.Date;

/**
 * GitHub issue milestone class.
 * @author Andrei Varabyeu
 */
public class Milestone {

    private Date createdAt;

    private Date dueOn;

    private int closedIssues;

    private int number;

    private int openIssues;

    private String description;

    private String state;

    private String title;

    private String url;

    private User creator;

    /**
     * @return createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Created at
     * @return this milestone
     */
    public Milestone setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @return dueOn
     */
    public Date getDueOn() {
        return dueOn;
    }

    /**
     * @param dueOn Due on
     * @return this milestone
     */
    public Milestone setDueOn(Date dueOn) {
        this.dueOn = dueOn;
        return this;
    }

    /**
     * @return closedIssues
     */
    public int getClosedIssues() {
        return closedIssues;
    }

    /**
     * @param closedIssues Closed Issues
     * @return this milestone
     */
    public Milestone setClosedIssues(int closedIssues) {
        this.closedIssues = closedIssues;
        return this;
    }

    /**
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number Number
     * @return this milestone
     */
    public Milestone setNumber(int number) {
        this.number = number;
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
     * @return this milestone
     */
    public Milestone setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
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
     * @return this milestone
     */
    public Milestone setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state State
     * @return this milestone
     */
    public Milestone setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title Title
     * @return this milestone
     */
    public Milestone setTitle(String title) {
        this.title = title;
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
     * @return this milestone
     */
    public Milestone setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return creator
     */
    public User getCreator() {
        return creator;
    }

    /**
     * @param creator Creator
     * @return this milestone
     */
    public Milestone setCreator(User creator) {
        this.creator = creator;
        return this;
    }
}

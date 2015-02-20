package com.github.avarabyeu.jashing.core;

import java.util.List;
import java.util.Map;

/**
 * Events and event sources configuration
 *
 * @author avarabyeu
 */
public class Configuration {

    /**
     * application-scope properties, might be injected into event source beans
     */
    private Map<String, String> properties;

    /**
     * events configuration
     */
    private List<EventConfig> events;

    private VCS vcs;

    public List<EventConfig> getEvents() {
        return events;
    }

    public void setEvents(List<EventConfig> events) {
        this.events = events;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public VCS getVcs() {
        return vcs;
    }

    public void setVcs(VCS vcs) {
        this.vcs = vcs;
    }

    public static class EventConfig {
        private String id;
        private String type;
        private long frequency;

        /**
         * Event-scope properties. Might be injected into event source bean related to this particular event
         */
        private Map<String, ?> properties;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getFrequency() {
            return frequency;
        }

        public void setFrequency(long frequency) {
            this.frequency = frequency;
        }

        public Map<String, ?> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, ?> properties) {
            this.properties = properties;
        }
    }

    public static class VCS {
        private List<SvnConfig> svns;
        private List<GitConfig> gits;

        public List<SvnConfig> getSvns() {
            return svns;
        }

        public void setSvns(List<SvnConfig> svns) {
            this.svns = svns;
        }

        public List<GitConfig> getGits() {
            return gits;
        }

        public void setGits(List<GitConfig> gits) {
            this.gits = gits;
        }
    }

    public static class VCSConfig {
        private String name;
        private String url;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SvnConfig extends VCSConfig {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class GitConfig extends VCSConfig {
        private String repoName;

        public String getRepoName() {
            return repoName;
        }

        public void setRepoName(String repoName) {
            this.repoName = repoName;
        }
    }


}

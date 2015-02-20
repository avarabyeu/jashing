package com.github.avarabyeu.jashing.integration.vcs;

import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public class VCSConfiguration {

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

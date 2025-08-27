package com.randomuser.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UserResponse {
    @JsonProperty("results")
    private List<User> results;

    @JsonProperty("info")
    private Info info;

    // Getters and setters
    public List<User> getResults() { return results; }
    public void setResults(List<User> results) { this.results = results; }

    public Info getInfo() { return info; }
    public void setInfo(Info info) { this.info = info; }

    public static class Info {
        private String seed;
        private int results;
        private int page;
        private String version;

        // Getters and setters
        public String getSeed() { return seed; }
        public void setSeed(String seed) { this.seed = seed; }

        public int getResults() { return results; }
        public void setResults(int results) { this.results = results; }

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}
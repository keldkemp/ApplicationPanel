package com.keldkemp.applicationpanel.github.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GitHubQuery {
    private String query;
    private int count;

    /**
     * Create query to DaData service.
     * @param query - text query;
     * @param count - max result count.
     */
    public GitHubQuery(String query, Integer count) {
        this.query = query;
        this.count = count == null ? 10 : count;
    }
}

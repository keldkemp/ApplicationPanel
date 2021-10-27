package com.keldkemp.applicationpanel.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.keldkemp.applicationpanel.github.domain.GitHubUserRepo;
import lombok.Data;

import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApplicationDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("original_name")
    private String originalName;
    @JsonProperty("github_url")
    private String gitHubUrl;
    @JsonProperty("last_update")
    private Date lastUpdate;
    @JsonProperty("status_applications")
    private Integer statusApplications;
    @JsonProperty("is_auto_update")
    private Integer isAutoUpdate;
    @JsonProperty("update_date_cron_format")
    private String updateDateCronFormat;
    @JsonProperty("github_repo")
    private GitHubUserRepo gitHubUserRepo;
    @JsonProperty("options")
    private OptionsDto options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getGitHubUrl() {
        return gitHubUrl;
    }

    public void setGitHubUrl(String gitHubUrl) {
        this.gitHubUrl = gitHubUrl;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getStatusApplications() {
        return statusApplications;
    }

    public void setStatusApplications(Integer statusApplications) {
        this.statusApplications = statusApplications;
    }

    public Integer getIsAutoUpdate() {
        return isAutoUpdate;
    }

    public void setIsAutoUpdate(Integer isAutoUpdate) {
        this.isAutoUpdate = isAutoUpdate;
    }

    public String getUpdateDateCronFormat() {
        return updateDateCronFormat;
    }

    public void setUpdateDateCronFormat(String updateDateCronFormat) {
        this.updateDateCronFormat = updateDateCronFormat;
    }

    public GitHubUserRepo getGitHubUserRepo() {
        return gitHubUserRepo;
    }

    public void setGitHubUserRepo(GitHubUserRepo gitHubUserRepo) {
        this.gitHubUserRepo = gitHubUserRepo;
    }

    public OptionsDto getOptions() {
        return options;
    }

    public void setOptions(OptionsDto options) {
        this.options = options;
    }
}

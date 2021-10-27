package com.keldkemp.applicationpanel.models;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Applications {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String originalName;

    @Column(nullable = false)
    private String gitHubUrl;

    private Date lastUpdate;

    private Integer statusApplications;

    @ColumnDefault("1")
    private Integer isAutoUpdate;

    private String updateDateCronFormat;

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
}

package ru.antoncharov.restapi.model;

import javax.persistence.*;

@Entity
public class JobInfo {
    private int jobId;
    private String status;
    private String filepath;

    public JobInfo() {
    }

    public JobInfo(int jobId, String status) {
        this.jobId = jobId;
        this.status = status;
    }

    @Id
    @Column(nullable = false, insertable = true, updatable = true)
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}

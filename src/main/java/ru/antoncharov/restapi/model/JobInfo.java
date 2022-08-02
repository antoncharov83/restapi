package ru.antoncharov.restapi.model;

import javax.persistence.*;
import java.util.UUID;
import ru.antoncharov.restapi.model.enums.JobState;

@Entity
public class JobInfo {

    @Id
    private UUID jobId;
    @Enumerated(EnumType.STRING)
    private JobState status;
    private String filepath;

    public JobInfo() {
    }

    public JobInfo(UUID jobId, JobState status) {
        this.jobId = jobId;
        this.status = status;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public JobState getStatus() {
        return status;
    }

    public void setStatus(JobState status) {
        this.status = status;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}

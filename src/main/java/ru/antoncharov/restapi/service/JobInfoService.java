package ru.antoncharov.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antoncharov.restapi.model.JobInfo;
import ru.antoncharov.restapi.repository.JobInfoRepository;

import java.util.Optional;

@Service
public class JobInfoService {
    private JobInfoRepository repository;

    @Autowired
    public JobInfoService(JobInfoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void setStatus(int jobId, String status){
        Optional<JobInfo> jobInfo = repository.findById(jobId);
        if(jobInfo.isPresent()){
            jobInfo.get().setStatus(status);
        }else {
            repository.saveAndFlush(new JobInfo(jobId, status));
        }
    }

    @Transactional(readOnly = true)
    public String getStatus(int jobId){
        Optional<JobInfo> jobInfo = repository.findById(jobId);
        if(jobInfo.isPresent()){
            return jobInfo.get().getStatus();
        }
        return null;
    }

    @Transactional
    public void setFilepath(int jobId, String filepath){
        Optional<JobInfo> jobInfo = repository.findById(jobId);
        if(jobInfo.isPresent()){
            jobInfo.get().setFilepath(filepath);
        }
    }

    @Transactional(readOnly = true)
    public String getFilepath(int jobId){
        Optional<JobInfo> jobInfo = repository.findById(jobId);
        if(jobInfo.isPresent()){
            return jobInfo.get().getFilepath();
        }
        return null;
    }
}

package ru.antoncharov.restapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.antoncharov.restapi.model.JobInfo;
import ru.antoncharov.restapi.model.enums.JobState;
import ru.antoncharov.restapi.repository.JobInfoRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobInfoService {
    private final JobInfoRepository repository;

    @Transactional
    public void setStatus(UUID jobId, JobState status){
        repository.findById(jobId)
                .ifPresentOrElse(ji -> ji.setStatus(status), () -> repository.saveAndFlush(new JobInfo(jobId, status)));
    }

    @Transactional(readOnly = true)
    public JobState getStatus(UUID jobId){
        return repository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getStatus();
    }

    @Transactional
    public void setFilepath(UUID jobId, String filepath){
        repository.findById(jobId)
                .ifPresentOrElse(ji -> ji.setFilepath(filepath), () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND);});
    }

    @Transactional(readOnly = true)
    public String getFilepath(UUID jobId){
        return repository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, jobId + " not found"))
                .getFilepath();
    }
}

package ru.antoncharov.restapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.antoncharov.restapi.model.enums.JobState;
import ru.antoncharov.restapi.storage.FileStorage;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobInfoService jobInfoService;

    private final FileStorage fileStorage;

    public UUID generate() {
        UUID jobId = UUID.randomUUID();
        jobInfoService.setStatus(jobId, JobState.IN_PROGRESS);
        fileStorage.generateFile(jobId);

        return jobId;
    }

    public UUID store(String filename) {
        UUID jobId = UUID.randomUUID();

        jobInfoService.setStatus(jobId, JobState.IN_PROGRESS);
        fileStorage.loadFromFile(jobId, filename);

        return jobId;
    }
}

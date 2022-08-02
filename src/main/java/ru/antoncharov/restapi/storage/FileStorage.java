package ru.antoncharov.restapi.storage;

import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.model.dto.SectionDto;
import ru.antoncharov.restapi.model.enums.JobState;
import ru.antoncharov.restapi.service.JobInfoService;
import ru.antoncharov.restapi.service.SectionService;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorage {

    private final Path rootLocation = Paths.get("filestorage");

    private final JobInfoService jobInfoService;
    private final XlsxHelper helper;
    private final SectionService sectionService;
    private final JobScheduler jobScheduler;

    public UUID store(MultipartFile file) {
        UUID jobId = UUID.randomUUID();

        jobInfoService.setStatus(jobId, JobState.IN_PROGRESS);
        jobScheduler.enqueue(jobId, () -> this.loadFromFile(jobId, file.getOriginalFilename()));

        return jobId;
    }

    public void loadFromFile(UUID jobId, String filename) {
        try {
            Path path = rootLocation.resolve(filename);
            List<SectionDto> sections = helper.readFromExcel(path.toString());
            sections.forEach(section -> {
                if (section.getId() == null) {
                    sectionService.save(section);
                    return;
                }
                sectionService.update(section);
            });
            jobInfoService.setStatus(jobId, JobState.DONE);
        } catch (Exception e) {
            jobInfoService.setStatus(jobId, JobState.ERROR);
        }
    }

    public UUID generate() {
        UUID jobId = UUID.randomUUID();
        jobInfoService.setStatus(jobId, JobState.IN_PROGRESS);
        jobScheduler.enqueue(() -> this.generateFile(jobId));

        return jobId;
    }

    public void generateFile(UUID jobId) {
        try {
            List<Section> sections = sectionService.getAll();
            String filepath = helper.generateXlsx(sections, jobId);
            jobInfoService.setFilepath(jobId, filepath);
            jobInfoService.setStatus(jobId, JobState.DONE);
        } catch (Exception e) {
            jobInfoService.setStatus(jobId, JobState.ERROR);
        }
    }

    public Resource loadFile(UUID generateJobId) {
        try {
            JobState status = jobInfoService.getStatus(generateJobId);

            if(!JobState.DONE.equals(status)) {
                throw new ResponseStatusException(HttpStatus.TOO_EARLY);
            }

            String filename = jobInfoService.getFilepath(generateJobId);
            Path file = Paths.get(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't read file");
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Bad filename");
        }
    }
}

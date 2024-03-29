package ru.antoncharov.restapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.antoncharov.restapi.model.dto.JobDto;
import ru.antoncharov.restapi.model.enums.JobState;
import ru.antoncharov.restapi.service.JobInfoService;
import ru.antoncharov.restapi.service.JobService;
import ru.antoncharov.restapi.storage.FileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ImportController {
    private final Path rootLocation = Paths.get("filestorage");

    private final FileStorage fileStorage;
    private final JobService jobService;
    private final JobInfoService jobInfoService;

    @PostMapping(value = "/import")
    public JobDto importFile(@RequestParam("file") MultipartFile file){
        Path path = rootLocation.resolve(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ResponseEntity.ok().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new JobDto(jobService.store(file.getOriginalFilename()), JobState.IN_PROGRESS);
    }

    @GetMapping(value = {"/import/{id}", "/export/{id}"})
    public JobDto processState(@PathVariable UUID id){
        return new JobDto(id, jobInfoService.getStatus(id));
    }

    @GetMapping(value = "/export")
    public JobDto exportFile(){
        return new JobDto(jobService.generate(), JobState.IN_PROGRESS);
    }

    @GetMapping(value = "/export/{id}/file")
    public ResponseEntity<Resource> exportFile(@PathVariable UUID id){
        return new ResponseEntity(fileStorage.loadFile(id), HttpStatus.OK);
    }
}

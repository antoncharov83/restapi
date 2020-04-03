package ru.antoncharov.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.antoncharov.restapi.service.JobInfoService;
import ru.antoncharov.restapi.storage.FileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Controller
public class ImportController {
    private final Path rootLocation = Paths.get("filestorage");

    private FileStorage fileStorage;
    private JobInfoService jobInfoService;

    @Autowired
    public ImportController(FileStorage fileStorage, JobInfoService jobInfoService) {
        this.fileStorage = fileStorage;
        this.jobInfoService = jobInfoService;
    }

    @PostMapping(value = "/import")
    public ResponseEntity importFile(@RequestParam("file") MultipartFile file){
        Path path = rootLocation.resolve(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ResponseEntity.ok().body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        fileStorage.store(file);
        return ResponseEntity.ok().body(file.hashCode());
    }

    @GetMapping(value = {"/import/{id}", "/export/{id}"})
    public ResponseEntity processState(@PathVariable int id){
        String status = jobInfoService.getStatus(id);
        if(status == null){
            return ResponseEntity.ok().body(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().body(jobInfoService.getStatus(id));
    }

    @GetMapping(value = "/export")
    public ResponseEntity exportFile(){
        int jobId = Math.abs(new Random().nextInt());
        fileStorage.generate(jobId);
        return ResponseEntity.ok().body(jobId);
    }

    @GetMapping(value = "/export/{id}/file")
    public ResponseEntity<Resource> exportFile(@PathVariable int id){
        String status = jobInfoService.getStatus(id);

        if(status == null){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        if(status.equals("DONE")){
            Resource file = fileStorage.loadFile(id);
            if(file == null){
                return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity(file, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(null);
    }
}

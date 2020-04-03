package ru.antoncharov.restapi.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.service.JobInfoService;
import ru.antoncharov.restapi.service.SectionService;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;

@Service
public class FileStorage {

    private final Path rootLocation = Paths.get("filestorage");

    private JobInfoService jobInfoService;
    private XlsxHelper helper;
    private SectionService sectionService;

    @Autowired
    public FileStorage(JobInfoService jobInfoService, XlsxHelper xlsxHelper, SectionService sectionService) {
        this.jobInfoService = jobInfoService;
        this.helper = xlsxHelper;
        this.sectionService = sectionService;
    }

    @Async
    public void store(MultipartFile file){
        try {
            jobInfoService.setStatus(file.hashCode(),"IN_PROGRESS");
            Path path = rootLocation.resolve(file.getOriginalFilename());
            List<Section> sections = helper.readFromExcel(path.toString());
            sectionService.saveAll(sections);
            jobInfoService.setStatus(file.hashCode(),"DONE");
        } catch (Exception e) {
            jobInfoService.setStatus(file.hashCode(),"ERROR");;
        }
    }

    @Async
    public void generate(int generateJobId){
        try {
            jobInfoService.setStatus(generateJobId,"IN_PROGRESS");
            List<Section> sections = sectionService.getAll();
            String filepath = helper.generateXlsx(sections, generateJobId);
            jobInfoService.setFilepath(generateJobId, filepath);
            jobInfoService.setStatus(generateJobId,"DONE");
        } catch (Exception e) {
            jobInfoService.setStatus(generateJobId,"ERROR");
        }
    }

    public Resource loadFile(int generateJobId) {
        try {
            String filename = jobInfoService.getFilepath(generateJobId);
            if(filename == null){
                return null;
            }
            Path file = Paths.get(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }
}

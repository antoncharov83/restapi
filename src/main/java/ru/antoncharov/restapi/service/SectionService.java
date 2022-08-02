package ru.antoncharov.restapi.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.antoncharov.restapi.mapper.SectionMapper;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.model.dto.SectionDto;
import ru.antoncharov.restapi.repository.GeologicalClassRepository;
import ru.antoncharov.restapi.repository.SectionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository repository;

    private final GeologicalClassRepository geologicalClassRepository;

    private final SectionMapper sectionMapper;

    @Transactional(readOnly = true)
    public Section get(@NonNull Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id + " section not found"));
    }

    @Transactional(readOnly = true)
    public List<Section> getAll(){
        return repository.findAll();
    }

    @Transactional
    public Section save(SectionDto sectionDto){
        if (sectionDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new section must don't have id");
        }
        sectionDto.getGeologicalClasses().stream().filter(gc -> gc.getId() != null).findAny()
                .ifPresent(gc -> { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new geologicalClasses must don't have id");});
        Section section = sectionMapper.fromDto(sectionDto);
        geologicalClassRepository.saveAll(section.getGeologicalClasses());
        return repository.save(section);
    }

    @Transactional
    public void delete(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public Section update(SectionDto sectionDto){
        if (sectionDto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "section id must be not null");
        }
//        repository.findById(sectionDto.getId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, sectionDto.getId() + " section not found"));
        Section section = sectionMapper.fromDto(sectionDto);
        if (repository.count(Example.of(section)) != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sectionDto.getId() + " section not found");
        }
        geologicalClassRepository.saveAll(section.getGeologicalClasses());
        return repository.save(section);
    }

    @Transactional(readOnly = true)
    public List<Section> getAllByCode(String code){
        return repository.findAllByGeologicalClasses_codeLikeIgnoreCase(code);
    }
}

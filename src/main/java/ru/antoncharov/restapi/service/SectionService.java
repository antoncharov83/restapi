package ru.antoncharov.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antoncharov.restapi.exception.CodeNotUniqueException;
import ru.antoncharov.restapi.model.GeologicalClass;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.repository.GeologicalClassRepository;
import ru.antoncharov.restapi.repository.SectionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private SectionRepository repository;

    private GeologicalClassRepository geologicalClassRepository;

    @Autowired
    public SectionService(SectionRepository repository, GeologicalClassRepository geologicalClassRepository) {
        this.repository = repository;
        this.geologicalClassRepository = geologicalClassRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Section> get(long id){
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Section> getAll(){
        return repository.findAll();
    }

    @Transactional
    public void saveAll(List<Section> sections){
        sections.forEach(section -> save(section));
    }

    @Transactional
    public Optional<Section> save(Section section){
        Optional<Section> new_section = Optional.of(repository.save(section));

        if(new_section.isPresent()) {
            section.getGeologicalClasses().forEach(g -> {
                Optional<GeologicalClass> found = geologicalClassRepository
                        .findByCodeEquals(g.getCode());
                if(!found.isPresent()) {
                    geologicalClassRepository.save(g);
                }else{
                    g.setId(found.get().getId());
                }
            });
            repository.saveAndFlush(section);
        }
        return new_section;
    }

    @Transactional
    public void delete(Section section){
        repository.delete(section);
    }

    @Transactional
    public Optional<Section> update(Section section){
        Optional<Section> found = repository.findById(section.getId());
        if(found.isPresent()){
            found.get().setName(section.getName());
            section.getGeologicalClasses().forEach(g -> {
                Optional<GeologicalClass> found_g = geologicalClassRepository
                        .findById(g.getId());
                if(!found_g.isPresent()){
                    try {
                        geologicalClassRepository.saveAndFlush(g);
                    }catch(Exception e){
                        throw new CodeNotUniqueException();
                    }
                }
            });
            found.get().setGeologicalClasses(section.getGeologicalClasses());
            repository.saveAndFlush(found.get());
        }
        return found;
    }

    @Transactional(readOnly = true)
    public List<Section> getAllByCode(String code){
        return repository.findAllByGeologicalClasses_codeLikeIgnoreCase(code);
    }
}

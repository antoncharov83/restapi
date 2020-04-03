package ru.antoncharov.restapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antoncharov.restapi.model.GeologicalClass;
import ru.antoncharov.restapi.repository.GeologicalClassRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GeologicalClassService {

    private GeologicalClassRepository repository;

    @Autowired
    public GeologicalClassService(GeologicalClassRepository repository) {
        this.repository = repository;
    }

    public Optional<GeologicalClass> get(Long id){
        return Optional.of(repository.getOne(id));
    }

    @Transactional(readOnly = true)
    public List<GeologicalClass> getAll(){
        return repository.findAll();
    }

    public Optional<GeologicalClass> save(GeologicalClass geologicalClass){
        return Optional.of(repository.saveAndFlush(geologicalClass));
    }

    public Optional<GeologicalClass> update(GeologicalClass geologicalClass){
        Optional<GeologicalClass> found = repository.findById(geologicalClass.getId());
        if (found.isPresent()) {
            repository.saveAndFlush(geologicalClass);
        }
        return found;
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}

package ru.antoncharov.restapi.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.antoncharov.restapi.mapper.GeologicalClassMapper;
import ru.antoncharov.restapi.model.GeologicalClass;
import ru.antoncharov.restapi.model.dto.GeologicalClassDto;
import ru.antoncharov.restapi.repository.GeologicalClassRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeologicalClassService {

    private final GeologicalClassRepository repository;

    private final GeologicalClassMapper geologicalClassMapper;

    @Transactional(readOnly = true)
    public List<GeologicalClass> getAll(){
        return repository.findAll();
    }

    public GeologicalClass save(GeologicalClassDto geologicalClass){
        return repository.save(geologicalClassMapper.fromDto(geologicalClass));
    }

    @Transactional
    public GeologicalClass update(GeologicalClassDto geologicalClass) {
        if (geologicalClass.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id can't be null");
        }
        GeologicalClass found = repository.findById(geologicalClass.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, geologicalClass.getId() + " - not found"));
        found.setCode(geologicalClass.getCode());
        found.setName(geologicalClass.getName());

        return repository.save(found);
    }

    public void delete(@NonNull Long code){
        repository.deleteById(code);
    }
}

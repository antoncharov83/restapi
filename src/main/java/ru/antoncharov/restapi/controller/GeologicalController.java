package ru.antoncharov.restapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.antoncharov.restapi.mapper.GeologicalClassMapper;
import ru.antoncharov.restapi.model.dto.GeologicalClassDto;
import ru.antoncharov.restapi.service.GeologicalClassService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/geologicals")
@RequiredArgsConstructor
public class GeologicalController {
    private final GeologicalClassService geologicalClassService;

    private final GeologicalClassMapper geologicalClassMapper;

    @GetMapping
    public List<GeologicalClassDto> getAll(){
        return geologicalClassService.getAll().stream()
                .map(geologicalClass -> geologicalClassMapper.toDto(geologicalClass))
                .collect(Collectors.toList());
    }

    @PostMapping
    public GeologicalClassDto create(@RequestBody GeologicalClassDto geologicalClass){
        return geologicalClassMapper.toDto(geologicalClassService.save(geologicalClass));
    }

    @PutMapping
    public GeologicalClassDto update(@RequestBody GeologicalClassDto geologicalClass){
        return geologicalClassMapper.toDto(geologicalClassService.update(geologicalClass));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody GeologicalClassDto geologicalClass){
        geologicalClassService.delete(ofNullable(geologicalClass.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "id can't be null")));
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}

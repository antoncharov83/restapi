package ru.antoncharov.restapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.antoncharov.restapi.mapper.SectionMapper;
import ru.antoncharov.restapi.model.dto.SectionDto;
import ru.antoncharov.restapi.service.SectionService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    private final SectionMapper sectionMapper;

    @PostMapping
    public SectionDto create(@RequestBody SectionDto section) {
        return sectionMapper.toDto(sectionService.save(section));
    }

    @GetMapping(value = "/{id}")
    public SectionDto get(@PathVariable(name = "id") long id){
        return sectionMapper.toDto(sectionService.get(id));
    }

    @GetMapping
    public List<SectionDto> getAll(){
        return sectionService.getAll().stream().map(s -> sectionMapper.toDto(s)).collect(Collectors.toList());
    }

    @PutMapping
    public SectionDto update(@RequestBody SectionDto section){
        return sectionMapper.toDto(sectionService.update(section));
    }

    @DeleteMapping
    public void delete(@RequestBody SectionDto section){
        sectionService.delete(ofNullable(section.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "id can't be null")));
    }

    @GetMapping(value = "/by-code")
    public List<SectionDto> getByCode(@RequestParam(name = "code") String code){
        return sectionService.getAllByCode(code).stream().map(section -> sectionMapper.toDto(section)).collect(Collectors.toList());
    }
}

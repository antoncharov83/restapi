package ru.antoncharov.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {
    private SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Section> create(@RequestBody Section section) {
        return sectionService.save(section).map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Section> get(@PathVariable(name = "id") long id){
        return sectionService.get(id).map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(new Section(), HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Section>> getAll(){
        List<Section> sections = sectionService.getAll();
        return new ResponseEntity<>(sections, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Section> update(@RequestBody Section section){
        return sectionService.update(section).map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public ResponseEntity<Section> delete(@RequestBody Section section){
        ResponseEntity<Section> del_section = sectionService.get(section.getId()).map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(new Section(), HttpStatus.NOT_FOUND));
        sectionService.delete(section);
        return del_section;
    }

    @GetMapping(value = "/by-code", produces = "application/json")
    public ResponseEntity<List<Section>> getByCode(@RequestParam(name = "code") String code){
        List<Section> found = sectionService.getAllByCode(code);
        return found.size() != 0 ?
                new ResponseEntity<>(found, HttpStatus.OK) : new ResponseEntity<>(found, HttpStatus.NOT_FOUND);
    }
}

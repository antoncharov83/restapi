package ru.antoncharov.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.antoncharov.restapi.model.GeologicalClass;
import ru.antoncharov.restapi.service.GeologicalClassService;

import java.util.List;

@RestController
@RequestMapping("/geologicals")
public class GeologicalController {
    private GeologicalClassService geologicalClassService;

    @Autowired
    public GeologicalController(GeologicalClassService geologicalClassService) {
        this.geologicalClassService = geologicalClassService;
    }

    @GetMapping
    public ResponseEntity<List<GeologicalClass>> getAll(){
        return new ResponseEntity<>(geologicalClassService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GeologicalClass> create(@RequestBody GeologicalClass geologicalClass){
        return geologicalClassService.save(geologicalClass).map(g -> new ResponseEntity<>(g, HttpStatus.OK))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping
    public ResponseEntity<GeologicalClass> update(@RequestBody GeologicalClass geologicalClass){
        return geologicalClassService.update(geologicalClass).map(g -> new ResponseEntity<>(g, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody GeologicalClass geologicalClass){
        geologicalClassService.delete(geologicalClass.getId());
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}

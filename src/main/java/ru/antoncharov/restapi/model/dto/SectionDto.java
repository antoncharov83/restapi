package ru.antoncharov.restapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {

    private Long id;

    private String name;

    private List<GeologicalClassDto> geologicalClasses = new ArrayList<>();

}

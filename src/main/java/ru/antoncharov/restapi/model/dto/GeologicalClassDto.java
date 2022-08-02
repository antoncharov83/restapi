package ru.antoncharov.restapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeologicalClassDto {

    private Long id;

    private String name;

    private String code;

}

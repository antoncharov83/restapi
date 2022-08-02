package ru.antoncharov.restapi.mapper;

import org.mapstruct.Mapper;
import ru.antoncharov.restapi.model.GeologicalClass;
import ru.antoncharov.restapi.model.dto.GeologicalClassDto;

@Mapper(componentModel = "spring")
public interface GeologicalClassMapper {

    GeologicalClass fromDto(GeologicalClassDto dto);
    GeologicalClassDto toDto(GeologicalClass geologicalClass);

}

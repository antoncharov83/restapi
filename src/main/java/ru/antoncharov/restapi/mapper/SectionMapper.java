package ru.antoncharov.restapi.mapper;

import org.mapstruct.Mapper;
import ru.antoncharov.restapi.model.Section;
import ru.antoncharov.restapi.model.dto.SectionDto;

@Mapper(uses = GeologicalClassMapper.class, componentModel = "spring")
public interface SectionMapper {
    Section fromDto(SectionDto dto);
    SectionDto toDto(Section section);
}

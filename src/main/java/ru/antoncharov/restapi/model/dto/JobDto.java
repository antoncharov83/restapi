package ru.antoncharov.restapi.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.antoncharov.restapi.model.enums.JobState;

import java.util.UUID;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDto {
    private UUID id;
    private JobState state;
}

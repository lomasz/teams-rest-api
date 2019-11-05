package com.lomasz.api.team.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewTeamDto {

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 5)
    private String acronym;

    private List<NewPlayerDto> players;

    @NotNull
    @Min(value = 0L, message = "The value must be positive")
    private Long budget;

    public NewTeamDto(String name, String acronym, Long budget) {
        this.name = name;
        this.acronym = acronym;
        this.budget = budget;
    }
}

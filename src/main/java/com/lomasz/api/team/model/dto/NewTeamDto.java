package com.lomasz.api.team.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewTeamDto {

    @NotNull
    private String name;

    @NotNull
    private String acronym;

    private List<NewPlayerDto> players;

    @NotNull
    private Long budget;

    public NewTeamDto(String name, String acronym, Long budget) {
        this.name = name;
        this.acronym = acronym;
        this.budget = budget;
    }
}

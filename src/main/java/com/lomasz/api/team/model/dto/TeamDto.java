package com.lomasz.api.team.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamDto {

    private Long id;

    private String name;

    private String acronym;

    private List<PlayerDto> players;

    private Long budget;

}

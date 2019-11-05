package com.lomasz.api.team.model.dto;

import com.lomasz.api.team.model.enums.Position;
import lombok.Data;

@Data
public class PlayerDto {

    private Long id;

    private String name;

    private Position position;

}

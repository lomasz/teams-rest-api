package com.lomasz.api.team.model.dto;

import com.lomasz.api.team.model.enums.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewPlayerDto {

    private String name;

    private Position position;

    public NewPlayerDto(String name, Position position) {
        this.name = name;
        this.position = position;
    }
}
package com.lomasz.api.team.mapper;

import com.lomasz.api.team.model.dto.NewPlayerDto;
import com.lomasz.api.team.model.dto.PlayerDto;
import com.lomasz.api.team.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    Player toEntity(NewPlayerDto dto);

    PlayerDto toPlayerDto(Player entity);

}

package com.lomasz.api.team.mapper;

import com.lomasz.api.team.model.dto.NewTeamDto;
import com.lomasz.api.team.model.dto.TeamDto;
import com.lomasz.api.team.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = PlayerMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {

    Team toEntity(NewTeamDto dto);

    TeamDto toTeamDto(Team entity);

    List<TeamDto> toTeamDtoList(List<Team> entities);

}

package com.lomasz.api.team.service;

import com.lomasz.api.team.mapper.TeamMapper;
import com.lomasz.api.team.model.dto.NewTeamDto;
import com.lomasz.api.team.model.dto.SearchResult;
import com.lomasz.api.team.model.dto.TeamDto;
import com.lomasz.api.team.model.entity.Team;
import com.lomasz.api.team.repository.TeamRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CommonsLog
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Autowired
    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    public SearchResult<TeamDto> getTeamList(Pageable pageable) {

        Page<Team> teamPage = teamRepository.findAll(pageable);
        List<TeamDto> items = teamMapper.toTeamDtoList(teamPage.getContent());

        return SearchResult.<TeamDto>builder()
                .items(items)
                .limit(pageable.getPageSize())
                .page(pageable.getPageNumber())
                .pages(teamPage.getTotalPages())
                .totalCount(teamPage.getTotalElements())
                .build();
    }

    public Long createTeam(NewTeamDto teamDto) {
        log.info("Saving new team started: " + teamDto);
        Team savedEntity = teamRepository.save(teamMapper.toEntity(teamDto));
        log.info("New team saved: " + savedEntity);
        return savedEntity.getId();
    }

    public Optional<TeamDto> findTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toTeamDto);
    }

}

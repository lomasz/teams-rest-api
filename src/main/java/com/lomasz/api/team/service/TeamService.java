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
        log.info("Getting list of teams from database for input: " + pageable);
        Page<Team> teamPage = teamRepository.findAll(pageable);
        log.info("Output:"
                + " pageable = " + teamPage.getPageable()
                + ", items = " + teamPage.getContent()
                + ", limit = " + teamPage.getPageable().getPageSize()
                + ", page = " + teamPage.getPageable().getPageNumber()
                + ", pages = " + teamPage.getTotalPages()
                + ", total count = " + teamPage.getTotalElements()
        );
        List<TeamDto> items = teamMapper.toTeamDtoList(teamPage.getContent());

        return SearchResult.<TeamDto>builder()
                .items(items)
                .limit(teamPage.getPageable().getPageSize())
                .page(teamPage.getPageable().getPageNumber())
                .pages(teamPage.getTotalPages())
                .totalCount(teamPage.getTotalElements())
                .build();
    }

    public Long createTeam(NewTeamDto teamDto) {
        log.info("Saving new team: " + teamDto);
        Team savedEntity = teamRepository.save(teamMapper.toEntity(teamDto));
        log.info("New team saved in database successfully: " + savedEntity);
        return savedEntity.getId();
    }

    public Optional<TeamDto> findTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toTeamDto);
    }

}

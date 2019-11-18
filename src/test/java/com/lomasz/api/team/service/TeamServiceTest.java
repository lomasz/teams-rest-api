package com.lomasz.api.team.service;

import com.lomasz.api.team.mapper.TeamMapper;
import com.lomasz.api.team.model.dto.NewTeamDto;
import com.lomasz.api.team.model.dto.SearchResult;
import com.lomasz.api.team.model.dto.TeamDto;
import com.lomasz.api.team.model.entity.Team;
import com.lomasz.api.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamService teamService;

    @Test
    void createTeam() {
        // given
        Long id = 1L;
        String name = "Olympique Gymnaste Club Nice";
        String acronym = "OGC";
        Long budget = 182005000L;

        NewTeamDto dto = new NewTeamDto(name, acronym, budget);

        Team newEntity = new Team();
        newEntity.setName(name);
        newEntity.setAcronym(acronym);
        newEntity.setBudget(budget);

        Team savedEntity = new Team();
        savedEntity.setId(id);
        savedEntity.setName(name);
        savedEntity.setAcronym(acronym);
        savedEntity.setBudget(budget);

        when(teamMapper.toEntity(dto)).thenReturn(newEntity);
        when(teamRepository.save(newEntity)).thenReturn(savedEntity);

        // when
        Long result = teamService.createTeam(dto);

        // then
        assertThat(result).isEqualTo(id);

        verify(teamMapper, times(1)).toEntity(dto);
        verify(teamRepository, times(1)).save(newEntity);
    }

    @Test
    void findTeamByIdWhenTeamEntityDoesntExistsShouldReturnOptionalEmpty() {
        // given
        Long id = 1L;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        // when
        Optional<TeamDto> result = teamService.findTeamById(id);

        // then
        assertFalse(result.isPresent());

        verify(teamRepository, times(1)).findById(id);
        verify(teamMapper, never()).toTeamDto(any(Team.class));
    }

    @Test
    void findTeamByIdWhenTeamEntityExistsShouldReturnOptionalTeamDto() {
        // given
        Long id = 1L;
        String name = "Olympique Gymnaste Club Nice";
        String acronym = "OGC";
        Long budget = 182005000L;

        Team entity = new Team();
        entity.setId(id);
        entity.setName(name);
        entity.setAcronym(acronym);
        entity.setBudget(budget);

        TeamDto dto = new TeamDto();
        dto.setId(id);
        dto.setName(name);
        dto.setAcronym(acronym);
        dto.setBudget(budget);

        when(teamRepository.findById(id)).thenReturn(Optional.of(entity));
        when(teamMapper.toTeamDto(entity)).thenReturn(dto);

        // when
        Optional<TeamDto> result = teamService.findTeamById(id);

        // then
        assertTrue(result.isPresent());
        assertThat(result.get()).isEqualTo(dto);

        verify(teamRepository, times(1)).findById(id);
        verify(teamMapper, times(1)).toTeamDto(entity);
    }

    @Test
    void getTeamList() {
        // given
        int page = 0;
        int limit = 5;

        PageRequest pageRequest = PageRequest.of(page, limit, Sort.Direction.DESC, "name");

        Long id = 1L;
        String name = "Olympique Gymnaste Club Nice";
        String acronym = "OGC";
        Long budget = 182005000L;

        Team entity = new Team();
        entity.setId(id);
        entity.setName(name);
        entity.setAcronym(acronym);
        entity.setBudget(budget);

        List<Team> entityList = Collections.singletonList(entity);

        TeamDto dto = new TeamDto();
        dto.setId(id);
        dto.setName(name);
        dto.setAcronym(acronym);
        dto.setBudget(budget);

        List<TeamDto> dtoList = Collections.singletonList(dto);

        long totalElements = 1L;
        int totalPages = 1;

        Pageable pageable = mock(Pageable.class);
        when(pageable.getPageSize()).thenReturn(limit);
        when(pageable.getPageNumber()).thenReturn(page);

        Page teamsPage = mock(Page.class);
        when(teamsPage.getTotalPages()).thenReturn(totalPages);
        when(teamsPage.getTotalElements()).thenReturn(totalElements);
        when(teamsPage.getPageable()).thenReturn(pageable);
        when(teamsPage.getContent()).thenReturn(entityList);

        when(teamRepository.findAll(any(Pageable.class))).thenReturn(teamsPage);
        when(teamMapper.toTeamDtoList(entityList)).thenReturn(dtoList);

        // when
        SearchResult<TeamDto> result = teamService.getTeamList(pageRequest);

        // then
        assertThat(result.getItems()).isEqualTo(dtoList);
        assertThat(result.getLimit()).isEqualTo(limit);
        assertThat(result.getPage()).isEqualTo(page);
        assertThat(result.getPages()).isEqualTo(totalPages);
        assertThat(result.getTotalCount()).isEqualTo(totalElements);

        verify(teamRepository, times(1)).findAll(any(Pageable.class));
        verify(teamMapper, times(1)).toTeamDtoList(entityList);
    }

}
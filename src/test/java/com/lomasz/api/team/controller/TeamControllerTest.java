package com.lomasz.api.team.controller;

import com.lomasz.api.team.model.dto.NewTeamDto;
import com.lomasz.api.team.model.dto.SearchResult;
import com.lomasz.api.team.model.dto.TeamDto;
import com.lomasz.api.team.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TeamController teamController;

    @Test
    void addTeamShouldReturnNewIdInLocationHeaderAndHttpStatusCreated() {
        // given
        Long id = 1L;
        NewTeamDto newTeamDto = new NewTeamDto("Olympique Gymnaste Club Nice", "OGC", 182005000L);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(teamService.createTeam(newTeamDto)).thenReturn(id);

        // when
        ResponseEntity result = teamController.addTeam(newTeamDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getHeaders().get("Location").get(0)).isEqualTo("/api/teams/" + id);

        verify(teamService, times(1)).createTeam(newTeamDto);
    }

    @Test
    void getTeamList() {
        // given
        int page = 0;
        int limit = 5;
        String sortName = "name";
        Sort.Direction sortOrder = Sort.Direction.DESC;
        Sort sort = Sort.by(sortOrder, sortName);

        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        TeamDto teamDto = new TeamDto();
        teamDto.setId(1L);
        teamDto.setName("Olympique Gymnaste Club Nice");
        teamDto.setAcronym("OGC");
        teamDto.setBudget(182005000L);

        SearchResult<TeamDto> searchResult = SearchResult.<TeamDto>builder()
                .items(Collections.singletonList(teamDto))
                .limit(limit)
                .page(page)
                .pages(1)
                .totalCount(1L)
                .build();

        when(teamService.getTeamList(pageRequest)).thenReturn(searchResult);

        // when
        ResponseEntity result = teamController.getTeamList(page, limit, sort.toString(), pageRequest);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(teamService, times(1)).getTeamList(pageRequest);
    }

    @Test
    void getTeamByIdWhenTeamDoesntExistShouldReturnHttpStatusNotFound() {
        // given
        Long id = 1L;

        when(teamService.findTeamById(id)).thenReturn(Optional.empty());

        // when
        ResponseEntity result = teamController.getTeamById(id);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(teamService, times(1)).findTeamById(id);
    }

    @Test
    void getTeamByIdWhenTeamExistsShouldReturnHttpStatusNotFound() {
        // given
        Long id = 1L;

        TeamDto teamDto = new TeamDto();
        teamDto.setId(id);
        teamDto.setName("Olympique Gymnaste Club Nice");
        teamDto.setAcronym("OGC");
        teamDto.setBudget(182005000L);

        when(teamService.findTeamById(id)).thenReturn(Optional.of(teamDto));

        // when
        ResponseEntity result = teamController.getTeamById(id);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(teamDto);

        verify(teamService, times(1)).findTeamById(id);
    }
}

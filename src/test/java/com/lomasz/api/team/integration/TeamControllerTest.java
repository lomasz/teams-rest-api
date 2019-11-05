package com.lomasz.api.team.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lomasz.api.team.model.dto.NewPlayerDto;
import com.lomasz.api.team.model.dto.NewTeamDto;
import com.lomasz.api.team.model.dto.PlayerDto;
import com.lomasz.api.team.model.dto.SearchResult;
import com.lomasz.api.team.model.dto.TeamDto;
import com.lomasz.api.team.model.entity.Player;
import com.lomasz.api.team.model.entity.Team;
import com.lomasz.api.team.model.enums.Position;
import com.lomasz.api.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamControllerTest {

    private static final String TEAM_GET_LIST_URL = "/api/teams";
    private static final String TEAM_GET_BY_ID_URL = "/api/teams/{id}";
    private static final String TEAM_CREATE_URL = "/api/teams";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @Transactional
    public void addTeam() throws Exception {
        // given
        String teamName = "Olympique Gymnaste Club Nice";
        String teamAcronym = "OGC";
        Long teamBudget = 182005000L;

        String playerName = "Youcef Atal";
        Position playerPosition = Position.RIGHT_FULLBACK;

        NewPlayerDto newPlayerDto = new NewPlayerDto(playerName, playerPosition);
        List<NewPlayerDto> newPlayers = Collections.singletonList(newPlayerDto);

        NewTeamDto newTeamDto = new NewTeamDto(teamName, teamAcronym, newPlayers, teamBudget);

        // when
        MvcResult result = mvc.perform(post(TEAM_CREATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTeamDto)))
                // then
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        Long id = Long.valueOf(result.getResponse().getHeader("Location").split("/")[5]);

        Team savedTeam = teamRepository.findById(id).get();

        assertNotNull(savedTeam);
        assertThat(savedTeam.getId()).isEqualTo(id);
        assertThat(savedTeam.getName()).isEqualTo(teamName);
        assertThat(savedTeam.getAcronym()).isEqualTo(teamAcronym);
        assertThat(savedTeam.getBudget()).isEqualTo(teamBudget);
        assertThat(savedTeam.getPlayers()).hasSize(1);

        Player savedPlayer = savedTeam.getPlayers().get(0);
        assertNotNull(savedPlayer.getId());
        assertThat(savedPlayer.getName()).isEqualTo(playerName);
        assertThat(savedPlayer.getPosition()).isEqualTo(playerPosition);
    }

    @Test
    @Transactional
    public void getTeamByIdWhenTeamExistsShouldReturnTeamDtoAndHttpStatusOk() throws Exception {
        // given
        String teamName = "Olympique Gymnaste Club Nice";
        String teamAcronym = "OGC";
        Long teamBudget = 182005000L;

        String playerName = "Youcef Atal";
        Position playerPosition = Position.RIGHT_FULLBACK;

        Player player = new Player();
        player.setName(playerName);
        player.setPosition(playerPosition);

        Team team = new Team();
        team.setName(teamName);
        team.setAcronym(teamAcronym);
        team.setBudget(teamBudget);
        team.setPlayers(Collections.singletonList(player));

        Team savedEntity = teamRepository.save(team);
        Long id = savedEntity.getId();

        // when
        MvcResult result = mvc.perform(get(TEAM_GET_BY_ID_URL, id)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andReturn();

        TeamDto teamDto = objectMapper.readValue(result.getResponse().getContentAsString(), TeamDto.class);

        assertThat(teamDto.getId()).isEqualTo(id);
        assertThat(teamDto.getName()).isEqualTo(teamName);
        assertThat(teamDto.getAcronym()).isEqualTo(teamAcronym);
        assertThat(teamDto.getBudget()).isEqualTo(teamBudget);
        assertThat(teamDto.getPlayers()).hasSize(1);

        PlayerDto playerDto = teamDto.getPlayers().get(0);
        assertNotNull(playerDto.getId());
        assertThat(playerDto.getName()).isEqualTo(playerName);
        assertThat(playerDto.getPosition()).isEqualTo(playerPosition);
    }

    @Test
    public void getTeamByIdWhenTeamDoesntExistShouldReturnHttpStatusNotFound() throws Exception {
        // given
        Long id = 99L;

        // when
        mvc.perform(get(TEAM_GET_BY_ID_URL, id)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void getTeamListWithDefaultInputShouldReturnUnsortedItems() throws Exception {
        // given
        Player youcefAtal = new Player();
        youcefAtal.setName("Youcef Atal");
        youcefAtal.setPosition(Position.RIGHT_FULLBACK);

        Team ogcNice = new Team();
        ogcNice.setName("Olympique Gymnaste Club Nice");
        ogcNice.setAcronym("OGC");
        ogcNice.setBudget(1000000L);
        ogcNice.setPlayers(Collections.singletonList(youcefAtal));

        Player neymar = new Player();
        neymar.setName("Neymar");
        neymar.setPosition(Position.LEFT_MIDFIELDER);

        Team psg = new Team();
        psg.setName("Paris Saint-Germain");
        psg.setAcronym("PSG");
        psg.setBudget(3000000L);
        psg.setPlayers(Collections.singletonList(neymar));

        Player anthonyLopes = new Player();
        anthonyLopes.setName("Anthony Lopes");
        anthonyLopes.setPosition(Position.GOALKEEPER);

        Team olympicLyon = new Team();
        olympicLyon.setName("Olympique Lyon");
        olympicLyon.setAcronym("OL");
        olympicLyon.setBudget(2000000L);
        olympicLyon.setPlayers(Collections.singletonList(anthonyLopes));

        teamRepository.save(ogcNice);
        teamRepository.save(psg);
        teamRepository.save(olympicLyon);

        // when
        MvcResult result = mvc.perform(get(TEAM_GET_LIST_URL)
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andReturn();

        SearchResult<TeamDto> searchResult = objectMapper
                .readValue(result.getResponse().getContentAsString(), new TypeReference<SearchResult<TeamDto>>() {
                });

        assertThat(searchResult.getTotalCount()).isEqualTo(3);
        assertThat(searchResult.getPage()).isEqualTo(0);
        assertThat(searchResult.getLimit()).isEqualTo(20);
        assertThat(searchResult.getPages()).isEqualTo(1);
        assertThat(searchResult.getItems()).hasSize(3);

    }

    @Test
    @Transactional
    public void getTeamListWithCustomInputShouldReturnItemsSortedByBudgetAsc() throws Exception {
        // given
        Player youcefAtal = new Player();
        youcefAtal.setName("Youcef Atal");
        youcefAtal.setPosition(Position.RIGHT_FULLBACK);

        Team ogcNice = new Team();
        ogcNice.setName("Olympique Gymnaste Club Nice");
        ogcNice.setAcronym("OGC");
        ogcNice.setBudget(1000000L);
        ogcNice.setPlayers(Collections.singletonList(youcefAtal));
        
        Player neymar = new Player();
        neymar.setName("Neymar");
        neymar.setPosition(Position.LEFT_MIDFIELDER);

        Team psg = new Team();
        psg.setName("Paris Saint-Germain");
        psg.setAcronym("PSG");
        psg.setBudget(3000000L);
        psg.setPlayers(Collections.singletonList(neymar));

        Player anthonyLopes = new Player();
        anthonyLopes.setName("Anthony Lopes");
        anthonyLopes.setPosition(Position.GOALKEEPER);

        Team olympicLyon = new Team();
        olympicLyon.setName("Olympique Lyon");
        olympicLyon.setAcronym("OL");
        olympicLyon.setBudget(2000000L);
        olympicLyon.setPlayers(Collections.singletonList(anthonyLopes));

        Player adrienSilva = new Player();
        adrienSilva.setName("Adrien Silva");
        adrienSilva.setPosition(Position.CENTRAL_MIDFIELDER);

        Team asMonaco = new Team();
        asMonaco.setName("AS Monaco");
        asMonaco.setAcronym("ASM");
        asMonaco.setBudget(4000000L);
        asMonaco.setPlayers(Collections.singletonList(adrienSilva));

        teamRepository.save(ogcNice);
        teamRepository.save(psg);
        teamRepository.save(olympicLyon);
        teamRepository.save(asMonaco);

        // when
        MvcResult result = mvc.perform(get(TEAM_GET_LIST_URL)
                .param("page", "1")
                .param("size", "2")
                .param("sort", "budget,desc")
                .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andReturn();

        SearchResult<TeamDto> searchResult = objectMapper
                .readValue(result.getResponse().getContentAsString(), new TypeReference<SearchResult<TeamDto>>() {
                });

        assertThat(searchResult.getTotalCount()).isEqualTo(4);
        assertThat(searchResult.getPage()).isEqualTo(1);
        assertThat(searchResult.getLimit()).isEqualTo(2);
        assertThat(searchResult.getPages()).isEqualTo(2);
        assertThat(searchResult.getItems()).isSortedAccordingTo(Comparator.comparingLong(TeamDto::getBudget).reversed());
    }
}

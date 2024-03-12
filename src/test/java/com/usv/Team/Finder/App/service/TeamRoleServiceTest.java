package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.TeamRoleDto;
import com.usv.Team.Finder.App.entity.TeamRole;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.repository.TeamRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TeamRoleServiceTest {

    @Mock
    private TeamRoleRepository teamRoleRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamRoleService teamRoleService;

    private UUID idOrganisation;
    private UUID idTeamRole;
    private TeamRole teamRole;
    private TeamRoleDto teamRoleDto;

    @BeforeEach
    void setUp() {
        idOrganisation = UUID.randomUUID();
        idTeamRole = UUID.randomUUID();

        teamRole = new TeamRole();
        teamRole.setIdTeamRole(idTeamRole);
        teamRole.setTeamRoleName("Developer");
        teamRole.setIdOrganisation(idOrganisation);

        teamRoleDto = new TeamRoleDto();
        teamRoleDto.setTeamRoleName("Developer");
        teamRoleDto.setIdOrganisation(idOrganisation);
    }

    @Test
    void getTeameRolesPerOrganisation_Success() {
        Mockito.when(teamRoleRepository.findByIdOrganisation(idOrganisation)).thenReturn(List.of(teamRole));
        List<TeamRole> result = teamRoleService.getTeameRolesPerOrganisation(idOrganisation);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(idOrganisation, result.get(0).getIdOrganisation());
    }

    @Test
    void getTeamRoleById_Success() {
        Mockito.when(teamRoleRepository.findById(idTeamRole)).thenReturn(Optional.of(teamRole));
        TeamRole result = teamRoleService.getTeamRoleById(idTeamRole);
        assertNotNull(result);
        assertEquals(idTeamRole, result.getIdTeamRole());
    }

    @Test
    void addTeamRole_Success() {
        User user = new User();
        user.setIdOrganisation(idOrganisation);
        Mockito.when(userService.existUser(any(UUID.class))).thenReturn(user);

        Mockito.when(teamRoleRepository.save(any(TeamRole.class))).thenReturn(teamRole);
        TeamRole result = teamRoleService.addTeamRole(idOrganisation, teamRoleDto);
        assertNotNull(result);
        assertEquals(teamRole.getTeamRoleName(), result.getTeamRoleName());
    }

    @Test
    void updateTeamRole_Success() {
        TeamRoleDto updatedDto = new TeamRoleDto();
        updatedDto.setTeamRoleName("Senior Developer");
        updatedDto.setIdOrganisation(idOrganisation);

        Mockito.when(teamRoleRepository.findById(idTeamRole)).thenReturn(Optional.of(teamRole));

        Mockito.when(teamRoleRepository.save(any(TeamRole.class))).thenAnswer(invocation -> {
            TeamRole savedTeamRole = invocation.getArgument(0);
            assertEquals("Senior Developer", savedTeamRole.getTeamRoleName());
            return savedTeamRole;
        });

        TeamRole result = teamRoleService.updateTeamRole(idTeamRole, updatedDto);
        assertNotNull(result);
        assertEquals("Senior Developer", result.getTeamRoleName());
    }


    @Test
    void deleteTeamRole_Success() {
        Mockito.when(teamRoleRepository.findById(idTeamRole)).thenReturn(Optional.of(teamRole));
        assertDoesNotThrow(() -> teamRoleService.deleteTeamRole(idTeamRole));
    }
}

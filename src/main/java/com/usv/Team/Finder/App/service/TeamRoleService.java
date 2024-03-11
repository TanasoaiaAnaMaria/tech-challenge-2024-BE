package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.TeamRoleDto;
import com.usv.Team.Finder.App.entity.TeamRole;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.TeamRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeamRoleService {
    private final TeamRoleRepository teamRoleRepository;
    private final OrganisationService organisationService;
    private final UserService userService;

    public TeamRoleService(TeamRoleRepository teamRoleRepository, OrganisationService organisationService, UserService userService) {
        this.teamRoleRepository = teamRoleRepository;
        this.organisationService = organisationService;
        this.userService = userService;
    }

    public List<TeamRole> getTeameRolesPerOrganisation(UUID idOrganisation) {
        List<TeamRole> teamRoles = teamRoleRepository.findByIdOrganisation(idOrganisation);

        return teamRoles.stream()
                .map(tRole -> TeamRole.builder()
                        .idTeamRole(tRole.getIdTeamRole())
                        .teamRoleName(tRole.getTeamRoleName())
                        .idOrganisation(tRole.getIdOrganisation())
                        .build())
                .collect(Collectors.toList());
    }

    public TeamRole getTeamRoleById(UUID idTeamRole){
        return teamRoleRepository.findById(idTeamRole).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_TEAMROLE));
    }

    public TeamRole addTeamRole(UUID idOrganisationAdmin, TeamRoleDto teamRoleDto){
        organisationService.getOrganisationById(teamRoleDto.getIdOrganisation());

        User user = userService.existUser(idOrganisationAdmin);
        if(!user.getIdOrganisation().equals(teamRoleDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);

        TeamRole teamRole = TeamRole.builder()
                .teamRoleName(teamRoleDto.getTeamRoleName())
                .idOrganisation(teamRoleDto.getIdOrganisation())
                .build();

        teamRoleRepository.save(teamRole);
        return teamRole;
    }

    public void updateTeamRole(UUID idTeamRole, TeamRoleDto teamRoleDto){
        TeamRole existingTeamRole = teamRoleRepository.findById(idTeamRole).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_TEAMROLE));

        if(!existingTeamRole.getIdOrganisation().equals(teamRoleDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);


        existingTeamRole.setTeamRoleName(teamRoleDto.getTeamRoleName());

        teamRoleRepository.save(existingTeamRole);
    }

    public void deleteTeamRole(UUID idTeamRole){
        if(teamRoleRepository.findById(idTeamRole).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_TEAMROLE);

        teamRoleRepository.deleteById(idTeamRole);
    }

}

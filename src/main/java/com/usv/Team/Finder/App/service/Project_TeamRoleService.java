package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.Project_TeamRoleDto;
import com.usv.Team.Finder.App.entity.Project_TeamRole;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.ProjectRepository;
import com.usv.Team.Finder.App.repository.Project_TeamRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class Project_TeamRoleService {
    public final Project_TeamRoleRepository projectTeamRoleRepository;
    public final ProjectRepository projectRepository;
    public final TeamRoleService teamRoleService;

    public Project_TeamRoleService(Project_TeamRoleRepository projectTeamRoleRepository, ProjectRepository projectRepository, TeamRoleService teamRoleService) {
        this.projectTeamRoleRepository = projectTeamRoleRepository;
        this.projectRepository = projectRepository;
        this.teamRoleService = teamRoleService;
    }

    public Project_TeamRole getProjectTeamRoleById(UUID idProject_TeamRole) {
        Project_TeamRole project_teamRole =  projectTeamRoleRepository.findByIdTeamRole(idProject_TeamRole).get(0);
        if(project_teamRole == null)
                throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_PROJECT_TEAM_ROLE);
        return project_teamRole;
    }

    public List<Project_TeamRole> findTeamRolesByProjectId(UUID projectId) {
        return projectTeamRoleRepository.findByIdProiect(projectId);
    }

    public Project_TeamRole addProjectTeamRole(Project_TeamRoleDto projectTeamRoleDto) {
        String teamRoleName = teamRoleService.getTeamRoleById(projectTeamRoleDto.getIdTeamRole()).getTeamRoleName();

        Project_TeamRole projectTeamRole = Project_TeamRole.builder()
                .idProiect(projectTeamRoleDto.getIdProiect())
                .projectName(projectTeamRoleDto.getProjectName())
                .idTeamRole(projectTeamRoleDto.getIdTeamRole())
                .teamRoleName(teamRoleName)
                .numberOfMembers(projectTeamRoleDto.getNumberOfMembers())
                .build();

        return projectTeamRoleRepository.save(projectTeamRole);
    }

    public void updateProjectTeamRole(UUID idProjectTeamRole, Project_TeamRoleDto teamRoleDto) {
        Project_TeamRole existingTeamRole =  projectTeamRoleRepository.findByIdTeamRole(idProjectTeamRole).get(0);
        if(existingTeamRole == null)
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_PROJECT_TEAM_ROLE);

        existingTeamRole.setNumberOfMembers(teamRoleDto.getNumberOfMembers());

        projectTeamRoleRepository.save(existingTeamRole);
    }

    public void deleteProjectTeamRole(UUID teamRoleId) {
        projectTeamRoleRepository.deleteById(teamRoleId);
    }
}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.ProjectDto;
import com.usv.Team.Finder.App.entity.Project;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectService {
    public final ProjectRepository projectRepository;

    public final Project_TeamRoleService projectTeamRoleService;
    public final UserService userService;

    public ProjectService(ProjectRepository projectRepository, Project_TeamRoleService projectTeamRoleService, UserService userService) {
        this.projectRepository = projectRepository;
        this.projectTeamRoleService = projectTeamRoleService;
        this.userService = userService;
    }
    public Project getProjectById(UUID idProject) {
        return projectRepository.findById(idProject).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_PROJECT));
    }

    public Project createProject(ProjectDto projectDto) {
        boolean isProjectManager = userService.isUserProjectManager(projectDto.getCreatedBy());
        if (!isProjectManager) {
            throw new FunctionalException(ApplicationConstants.ERROR_CREATE_PROJECT, HttpStatus.FORBIDDEN);
        }

        if (!projectDto.getProjectStatus().equals("Not Started") && !projectDto.getProjectStatus().equals("Starting")) {
            throw new FunctionalException(ApplicationConstants.ERROR_CREATE_PROJECT_STATUS, HttpStatus.BAD_REQUEST);
        }

        Project project = Project.builder()
                .projectName(projectDto.getProjectName())
                .projectPeriod(projectDto.getProjectPeriod())
                .startDate(projectDto.getStartDate())
                .deadlineDate(projectDto.getDeadlineDate())
                .generalDescription(projectDto.getGeneralDescription())
                .idOrganisation(projectDto.getIdOrganisation())
                .createdBy(projectDto.getCreatedBy())
                .projectStatus(projectDto.getProjectStatus())
                .canBeDeleted(true)
                .build();

        Project savedProject = projectRepository.save(project);

        projectDto.getTeamRoles().forEach(teamRoleDto -> {
            teamRoleDto.setIdProiect(savedProject.getIdProject());
            teamRoleDto.setProjectName(savedProject.getProjectName());
            projectTeamRoleService.addProjectTeamRole(teamRoleDto);
        });

        return savedProject;
    }
}

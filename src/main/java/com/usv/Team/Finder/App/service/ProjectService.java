package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.ProjectDto;
import com.usv.Team.Finder.App.dto.Project_TeamRoleDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Project;
import com.usv.Team.Finder.App.entity.Project_TeamRole;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<Project> getProjectsByCreator(UUID creatorId) {
        return projectRepository.findByCreatedBy(creatorId);
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
    @Transactional
    public Project updateProject(UUID projectId, ProjectDto projectDto) {
        Project existingProject = getProjectById(projectId);

        UUID currentUserId = projectDto.getCreatedBy();
        boolean isProjectManager = userService.isUserProjectManager(currentUserId);
        if (!existingProject.getCreatedBy().equals(currentUserId) || !isProjectManager) {
            throw new FunctionalException(ApplicationConstants.ERROR_UPDATE_PROJECT, HttpStatus.FORBIDDEN);
        }

        if (projectDto.getProjectStatus().equals("In Progress") || projectDto.getProjectStatus().equals("Closing") || projectDto.getProjectStatus().equals("Closed")) {
            existingProject.setCanBeDeleted(false);
        }

        existingProject.setProjectName(projectDto.getProjectName());
        existingProject.setProjectPeriod(projectDto.getProjectPeriod());
        existingProject.setStartDate(projectDto.getStartDate());
        existingProject.setDeadlineDate(projectDto.getDeadlineDate());
        existingProject.setGeneralDescription(projectDto.getGeneralDescription());
        existingProject.setProjectStatus(projectDto.getProjectStatus());

        Set<UUID> existingTeamRoleIds = existingProject.getTeamRoles().stream()
                .map(Project_TeamRole::getIdTeamRole)
                .collect(Collectors.toSet());

        for (Project_TeamRoleDto dto : projectDto.getTeamRoles()) {
            if (dto.getIdTeamRole() != null && existingTeamRoleIds.contains(dto.getIdTeamRole())) {

                projectTeamRoleService.updateProjectTeamRole(dto.getIdTeamRole(), dto);
            } else if (dto.getIdTeamRole() != null) {

                Project_TeamRole newTeamRole = projectTeamRoleService.addProjectTeamRole(dto);
                existingProject.getTeamRoles().add(newTeamRole);
            }
        }

        existingProject.getTeamRoles().removeIf(teamRole ->
                !projectDto.getTeamRoles().stream()
                        .map(Project_TeamRoleDto::getIdTeamRole)
                        .toList()
                        .contains(teamRole.getIdTeamRole()));

        return projectRepository.save(existingProject);
    }

    public void deleteProjectIfEligible(UUID projectId, UUID userId) {
        Project project = getProjectById(projectId);

        boolean isProjectManager = userService.isUserProjectManager(userId);
        if (!project.getCreatedBy().equals(userId) || !isProjectManager) {
            throw new FunctionalException(ApplicationConstants.ERROR_DELETE_PROJECT, HttpStatus.FORBIDDEN);
        }

        if (!project.getCanBeDeleted()) {
            throw new FunctionalException(ApplicationConstants.ERROR_DELETE_PROJECT, HttpStatus.BAD_REQUEST);
        }

        projectRepository.deleteById(projectId);
    }

}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.ProjectAssignmentDto;
import com.usv.Team.Finder.App.entity.ProjectAssignment;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.ProjectAssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectAssignmentService {
    private static final int MAX_WORK_HOURS_PER_DAY = 8;
    public final ProjectAssignmentRepository projectAssignmentRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final TeamRoleService teamRoleService;

    public ProjectAssignmentService(ProjectAssignmentRepository projectAssignmentRepository, ProjectService projectService, UserService userService, TeamRoleService teamRoleService) {
        this.projectAssignmentRepository = projectAssignmentRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.teamRoleService = teamRoleService;
    }

    public List<ProjectAssignment> getAssignmentsByProject(UUID idProject) {
        return projectAssignmentRepository.findByIdProject(idProject);
    }

    public List<ProjectAssignment> getActiveAssignmentsByUser(UUID idUser) {
        return projectAssignmentRepository.findByIdUserAndActive(idUser, true);
    }

    public ProjectAssignment getProjectAssignmentById(UUID idProjectAssignment) {
        return projectAssignmentRepository.findById(idProjectAssignment).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_PROJECT_ASSIGNMENT));
    }

    @Transactional
    public ProjectAssignment createProjectAssignment(ProjectAssignmentDto projectAssignmentDto) {
        userService.existUser(projectAssignmentDto.getIdUser());
        projectService.getProjectById(projectAssignmentDto.getIdProject());
        teamRoleService.getTeamRoleById(projectAssignmentDto.getIdTeamRole());

        List<ProjectAssignment> activeAssignments = projectAssignmentRepository.findByIdUserAndActive(projectAssignmentDto.getIdUser(), true);

        int assignedHours = 0;
        for (ProjectAssignment assignment : activeAssignments) {
            assignedHours += assignment.getWorkHours();
        }

        int availableHours = MAX_WORK_HOURS_PER_DAY - assignedHours;

        if (projectAssignmentDto.getWorkHours() > availableHours) {
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER_OVERASSIGNED);
        }

        ProjectAssignment projectAssignment = ProjectAssignment.builder()
                .idProject(projectAssignmentDto.getIdProject())
                .idTeamRole(projectAssignmentDto.getIdTeamRole())
                .idUser(projectAssignmentDto.getIdUser())
                .workHours(projectAssignmentDto.getWorkHours())
                .active(false)
                .build();

        return projectAssignmentRepository.save(projectAssignment);
    }

    public void deleteProjectAssignment(UUID idProjectAssignment){
        ProjectAssignment existingProjectAssignment = getProjectAssignmentById(idProjectAssignment);
        projectAssignmentRepository.delete(existingProjectAssignment);
    }
}

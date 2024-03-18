package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.ProjectDto;
import com.usv.Team.Finder.App.entity.Project;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/project")
@CrossOrigin("*")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/createdBy")
    public ResponseEntity<List<Project>> getProjectsCreatedByMe(@RequestParam UUID idUser) {
        List<Project> projects = projectService.getProjectsByCreator(idUser);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/getById")
    public ResponseEntity<Project> getProjectById(@RequestParam UUID idProject){
        try {
            Project project = projectService.getProjectById(idProject);
            return ResponseEntity.ok(project);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/createProject")
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto projectDto){
        try {
            Project project = projectService.createProject(projectDto);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/updateProject")
    public ResponseEntity<?> updateProject(@RequestParam UUID projectId, @RequestBody ProjectDto projectDto) {
        try {
            Project updatedProject = projectService.updateProject(projectId, projectDto);
            return ResponseEntity.ok(updatedProject);
        } catch (FunctionalException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

}

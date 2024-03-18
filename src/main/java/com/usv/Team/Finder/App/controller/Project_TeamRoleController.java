package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.entity.Project_TeamRole;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.Project_TeamRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/project_teamRole")
@CrossOrigin("*")
public class Project_TeamRoleController {
    private final Project_TeamRoleService projectTeamRoleService;

    public Project_TeamRoleController(Project_TeamRoleService projectTeamRoleService) {
        this.projectTeamRoleService = projectTeamRoleService;
    }

    @GetMapping("/getById")
    public ResponseEntity<Project_TeamRole> getProjectById(@RequestParam UUID idProject_TeamRole){
        try {
            Project_TeamRole project_teamRole = projectTeamRoleService.getProjectTeamRoleById(idProject_TeamRole);
            return ResponseEntity.ok(project_teamRole);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

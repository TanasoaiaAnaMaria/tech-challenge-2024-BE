package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.service.Project_TeamRoleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project_teamRole")
@CrossOrigin("*")
public class Project_TeamRoleController {
    private final Project_TeamRoleService projectTeamRoleService;

    public Project_TeamRoleController(Project_TeamRoleService projectTeamRoleService) {
        this.projectTeamRoleService = projectTeamRoleService;
    }
}

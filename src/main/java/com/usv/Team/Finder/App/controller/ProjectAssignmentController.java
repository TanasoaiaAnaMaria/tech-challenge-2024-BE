package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.service.ProjectAssignmentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projectAssignment")
@CrossOrigin("*")
public class ProjectAssignmentController {
    private final ProjectAssignmentService projectAssignmentService;

    public ProjectAssignmentController(ProjectAssignmentService projectAssignmentService) {
        this.projectAssignmentService = projectAssignmentService;
    }
}

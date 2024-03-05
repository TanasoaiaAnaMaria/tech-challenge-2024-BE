package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllDepartments(@RequestParam UUID idOrganisation){
        List<UserDto> users = userService.getUsersPerOrganisation(idOrganisation);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasAnyRole('ORGANISATION_ADMIN','DEPARTMENT_MANAGER','PROJECT_MANAGER','EMPLOYEE')")
    public ResponseEntity<UserDto> getDepartmentById(@RequestParam UUID idUser){
        try {
            UserDto user = userService.getUserById(idUser);
            return ResponseEntity.ok(user);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

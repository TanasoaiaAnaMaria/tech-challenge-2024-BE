package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<List<UserDto>> getAllUsersPerOrganisation(@RequestParam UUID idOrganisation) {
        List<UserDto> users = userService.getUsersPerOrganisation(idOrganisation);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasAnyRole('ORGANISATION_ADMIN','DEPARTMENT_MANAGER','PROJECT_MANAGER','EMPLOYEE')")
    public ResponseEntity<UserDto> getUserById(@RequestParam UUID idUser) {
        try {
            UserDto user = userService.getUserById(idUser);
            return ResponseEntity.ok(user);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/addRole")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<UserDto> addUserRole(@RequestParam UUID idUser, @RequestParam Long idRole) {
        try {
            userService.addUserRole(idUser, idRole);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/removeRole")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<UserDto> removeUserRole(@RequestParam UUID idUser, @RequestParam Long idRole) {
        try {
            userService.removeUserRole(idUser, idRole);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getUnassignedDepartmentManagers")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<List<UserDto>> getUnassignedDepartmentManagers() {
        List<UserDto> unassignedManagers = userService.getUnassignedDepartmentManagers();
        if (unassignedManagers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(unassignedManagers);
    }

    @GetMapping("/getUsersWithoutDepartment")
    @PreAuthorize("hasAnyRole('ORGANISATION_ADMIN','DEPARTMENT_MANAGER')")

    public ResponseEntity<List<UserDto>> getUsersWithoutDepartment() {
        List<UserDto> unassignedManagers = userService.getUsersWithoutDepartment();
        if (unassignedManagers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(unassignedManagers);
    }

    @PutMapping("/assignUserToDepartment")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<?> assignUserToDepartment(@RequestParam UUID idUser, @RequestParam UUID idDepartment) {
        try {
            userService.assignUserToDepartment(idUser, idDepartment);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/removeUserFromDepartment")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<UserDto> removeUserFromDepartment(@RequestParam UUID idUser) {
        try {
            userService.removeUserFromDepartment(idUser);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/addDepartmentManager")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<Department> addDepartmentManager(@RequestParam UUID idUser, @RequestParam UUID idDepartment) {
        userService.addDepartmentManager(idUser, idDepartment);
        return ResponseEntity.ok().build();
    }
}

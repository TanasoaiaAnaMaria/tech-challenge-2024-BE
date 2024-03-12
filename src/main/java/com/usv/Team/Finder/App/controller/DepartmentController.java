package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.DepartmentDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/department")
@CrossOrigin("*")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<List<Department>> getAllDepartments(@RequestParam UUID idOrganisation){
        List<Department> departments = departmentService.getDepartments(idOrganisation);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasAnyRole('ORGANISATION_ADMIN','DEPARTMENT_MANAGER')")
    public ResponseEntity<Department> getDepartmentById(@RequestParam UUID idDepartment){
        try {
            Department department = departmentService.getDepartmentById(idDepartment);
            return ResponseEntity.ok(department);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addDepartment")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<Department> addDepartment(@RequestParam UUID idOrganisationAdmin, @RequestBody DepartmentDto departmentDto){
        Department department = departmentService.addDepartment(idOrganisationAdmin, departmentDto);
        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }

    @PutMapping("/updateDepartent")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<Department> updateDepartment(@RequestParam UUID idDepartment, @RequestBody DepartmentDto departmentDto){
        Department department = departmentService.updateDepartment(idDepartment, departmentDto);
        return ResponseEntity.ok(department);
    }

    @DeleteMapping("/deleteDepartment")
    @PreAuthorize("hasRole('ORGANISATION_ADMIN')")
    public ResponseEntity<?> deleteDepartment(@RequestParam UUID idDepartment){
        try{
            departmentService.deleteDepartment(idDepartment);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

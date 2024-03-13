package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.SkilCategoryDto;
import com.usv.Team.Finder.App.entity.SkilCategory;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.SkilCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/skilCategory")
@CrossOrigin("*")
public class SkilCategoryController {
    private final SkilCategoryService skilCategoryService;

    public SkilCategoryController(SkilCategoryService skilCategoryService) {
        this.skilCategoryService = skilCategoryService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<List<SkilCategory>> getSkilCategoryPerOrganisation(@RequestParam UUID idOrganisation) {
        List<SkilCategory> skilCategories = skilCategoryService.getSkilCategoryPerOrganisation(idOrganisation);
        return ResponseEntity.ok(skilCategories);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkilCategory> getSkilCategoryById(@RequestParam UUID idSkilCategory){
        try {
            SkilCategory skilCategory = skilCategoryService.getSkilCategoryById(idSkilCategory);
            return ResponseEntity.ok(skilCategory);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addSkilCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkilCategory> addSkilCategory(@RequestParam UUID idOrganisationAdmin, @RequestBody SkilCategoryDto skilCategoryDto){
        SkilCategory skilCategory = skilCategoryService.addSkilCategory(idOrganisationAdmin, skilCategoryDto);
        return new ResponseEntity<>(skilCategory, HttpStatus.CREATED);
    }

    @PutMapping("/updateSkilCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkilCategory> updateSkilCategory(@RequestParam UUID idSkilCategory, @RequestBody SkilCategoryDto skilCategoryDto){
        skilCategoryService.updateSkilCategory(idSkilCategory, skilCategoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSkilCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> deleteSkilCategory(@RequestParam UUID idSkilCategory){
        try{
            skilCategoryService.deleteSkilCategory(idSkilCategory);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

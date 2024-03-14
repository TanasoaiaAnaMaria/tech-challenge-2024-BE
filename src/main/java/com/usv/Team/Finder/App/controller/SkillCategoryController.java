package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.SkillCategoryDto;
import com.usv.Team.Finder.App.entity.SkillCategory;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.SkillCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/skillCategory")
@CrossOrigin("*")
public class SkillCategoryController {
    private final SkillCategoryService skillCategoryService;

    public SkillCategoryController(SkillCategoryService skillCategoryService) {
        this.skillCategoryService = skillCategoryService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<List<SkillCategory>> getSkilCategoryPerOrganisation(@RequestParam UUID idOrganisation) {
        List<SkillCategory> skillCategories = skillCategoryService.getSkillCategoryPerOrganisation(idOrganisation);
        return ResponseEntity.ok(skillCategories);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkillCategory> getSkillCategoryById(@RequestParam UUID idSkilCategory){
        try {
            SkillCategory skillCategory = skillCategoryService.getSkillCategoryById(idSkilCategory);
            return ResponseEntity.ok(skillCategory);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addSkillCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkillCategory> addSkillCategory(@RequestParam UUID idOrganisationAdmin, @RequestBody SkillCategoryDto skillCategoryDto){
        SkillCategory skillCategory = skillCategoryService.addSkillCategory(idOrganisationAdmin, skillCategoryDto);
        return new ResponseEntity<>(skillCategory, HttpStatus.CREATED);
    }

    @PutMapping("/updateSkillCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkillCategory> updateSkillCategory(@RequestParam UUID idSkilCategory, @RequestBody SkillCategoryDto skillCategoryDto){
        skillCategoryService.updateSkillCategory(idSkilCategory, skillCategoryDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSkillCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> deleteSkillCategory(@RequestParam UUID idSkilCategory){
        try{
            skillCategoryService.deleteSkillCategory(idSkilCategory);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

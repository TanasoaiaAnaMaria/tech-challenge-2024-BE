package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.SkillDto;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/skill")
@CrossOrigin("*")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('DEPARTMENT_MANAGER','EMPLOYEE')")
    public ResponseEntity<List<SkillDto>> getAllSkillsByOrganisation(@RequestParam UUID idOrganisation, @RequestParam UUID idUser){
        List<SkillDto> skills = skillService.getAllSkillsByOrganisation(idOrganisation, idUser);
        return ResponseEntity.ok(skills);
    }
    @GetMapping("/getById")
    @PreAuthorize("hasAnyRole('DEPARTMENT_MANAGER','EMPLOYEE')")
    public ResponseEntity<SkillDto> getSkillById(@RequestParam UUID idSkill, @RequestParam UUID idUser){
        try {
            SkillDto skill = skillService.getSkillById(idSkill, idUser);
            return ResponseEntity.ok(skill);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByDepartment")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> getSkillsByDepartment(@RequestParam UUID idUser) {
        try {
            List<SkillDto> skills = skillService.getSkillsByDepartment(idUser);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getSkillsByCategory")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> getSkillsByCategory(@RequestParam UUID idCategory, @RequestParam UUID idUser) {
        try {
            List<SkillDto> skills = skillService.getSkillsByCategory(idCategory, idUser);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/addSkill")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<Skill> addSkil(@RequestBody SkillDto skillDto){
        skillService.addSkill(skillDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/addSkillToMyDepartment")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> addSkillToMyDepartment(@RequestParam UUID idSkill, @RequestParam UUID idUser) {
        try {
            Skill skill = skillService.addSkillToMyDepartment(idSkill, idUser);
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } catch (CrudOperationException | FunctionalException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/removeSkillFromMyDepartment")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> removeSkillFromMyDepartment(@RequestParam UUID idSkill, @RequestParam UUID idUser) {
        try {
            skillService.removeSkillFromMyDepartment(idSkill, idUser);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException | FunctionalException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/updateSkill")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<Skill> updateSkill(@RequestParam UUID idSkill, @RequestParam UUID idUser, @RequestBody SkillDto skillDto){
        Skill skill = skillService.updateSkill(idSkill, idUser, skillDto);
        return ResponseEntity.ok(skill);
    }

    @DeleteMapping("/deleteSkill")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> deleteSkill(@RequestParam UUID idSkill, @RequestParam UUID idUser){
        try{
            skillService.deleteSkill(idSkill,idUser);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

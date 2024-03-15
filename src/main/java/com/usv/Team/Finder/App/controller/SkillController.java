package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.SkillDto;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/skill")
@CrossOrigin("*")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<SkillDto> getSkillById(@RequestParam UUID idSkill){
        try {
            SkillDto skill = skillService.getSkillById(idSkill);
            return ResponseEntity.ok(skill);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addSkill")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<Skill> addSkil(@RequestBody SkillDto skillDto){
        skillService.addSkill(skillDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}

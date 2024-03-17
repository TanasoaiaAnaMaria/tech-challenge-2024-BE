package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.User_SkillDto;
import com.usv.Team.Finder.App.entity.User_Skill;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.service.User_SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/user_skill")
@CrossOrigin("*")
public class User_SkillController {
    private final User_SkillService userSkillService;

    public User_SkillController(User_SkillService userSkillService) {
        this.userSkillService = userSkillService;
    }

    @GetMapping("/getByUser")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User_Skill>> getUserSkillsByUser(@RequestParam UUID idUser) {
        List<User_Skill> userSkills = userSkillService.getUserSkillsByUser(idUser);
        return ResponseEntity.ok(userSkills);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User_Skill> getUserSkillById(@RequestParam UUID idUserSkill){
        try {
            User_Skill userSkill = userSkillService.getUserSkillById(idUserSkill);
            return ResponseEntity.ok(userSkill);
        } catch (CrudOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addUserSkill")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User_Skill> addUserSkill(@RequestBody User_SkillDto userSkillDto){
        User_Skill userSkill = userSkillService.addUserSkill(userSkillDto);
        return new ResponseEntity<>(userSkill, HttpStatus.CREATED);
    }

    @PutMapping("/updateUserSkill")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User_Skill> updateUserSkill(@RequestParam UUID idUserSkill, @RequestBody User_SkillDto userSkillDto){
        userSkillService.updateUserSkill(idUserSkill, userSkillDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteUserSkill")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUserSkill(@RequestParam UUID idUserSkill){
        try{
            userSkillService.deleteUserSkill(idUserSkill);
            return ResponseEntity.ok().build();
        } catch (CrudOperationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.SkillDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    private final DepartmentService departmentService;

    private final UserService userService;

    public SkillService(SkillRepository skillRepository, DepartmentService departmentService, UserService userService) {
        this.skillRepository = skillRepository;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    public Skill addSkill(SkillDto skillDto){
        User user =  userService.existUser(skillDto.getCreatedBy());
        Department department = departmentService.getDepartmentById(user.getIdDepartment());

        Skill skill = Skill.builder()
                .skillName(skillDto.getSkilName())
                .skillDescription(skillDto.getSkilDescription())
                .createdBy(skillDto.getCreatedBy())
                .idSkillCategory(skillDto.getIdSkilCategory())
                .build();

        if (Boolean.TRUE.equals(skillDto.getAdToMyDepartment())) {

            if (skill.getDepartments() == null) {
                skill.setDepartments(new HashSet<>());
            }

            skill.getDepartments().add(department);
        }

        skillRepository.save(skill);
        departmentService.addSkill(department.getIdDepartment(),skill);
        return skill;
    }
}

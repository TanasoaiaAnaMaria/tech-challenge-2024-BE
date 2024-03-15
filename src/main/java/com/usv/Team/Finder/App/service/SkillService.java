package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.SkillDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    private final DepartmentService departmentService;

    private final UserService userService;
    private final SkillCategoryService skillCategoryService;

    public SkillService(SkillRepository skillRepository, DepartmentService departmentService, UserService userService, SkillCategoryService skillCategoryService) {
        this.skillRepository = skillRepository;
        this.departmentService = departmentService;
        this.userService = userService;
        this.skillCategoryService = skillCategoryService;
    }

    public SkillDto getSkillById(UUID idSkill){
        Skill skill = skillRepository.findById(idSkill).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL));

        return SkillDto.builder()
                .skillName(skill.getSkillName())
                .skillDescription(skill.getSkillDescription())
                .creatorName(userService.getSkilCreatorName(skill.getCreatedBy()))
                .skillCategoryName(skillCategoryService.getSkillCategoryById(skill.getIdSkillCategory()).getSkilCategoryName())
                .departments(departmentService.findDepartmentNamesBySkillAndOrganisation(idSkill, skill.getIdOrganisation()))
                .build();
    }

    public Skill addSkill(SkillDto skillDto){
        User user =  userService.existUser(skillDto.getCreatedBy());
        Department department = departmentService.getDepartmentById(user.getIdDepartment());

        Skill skill = Skill.builder()
                .skillName(skillDto.getSkillName())
                .idOrganisation(user.getIdOrganisation())
                .skillDescription(skillDto.getSkillDescription())
                .createdBy(skillDto.getCreatedBy())
                .idSkillCategory(skillDto.getIdSkilCategory())
                .build();

        if (Boolean.TRUE.equals(skillDto.getAdToMyDepartment()) && user.getIsDepartmentManager()) {

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

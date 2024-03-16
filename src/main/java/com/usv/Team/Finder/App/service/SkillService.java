package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.SkillDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.SkillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private SkillDto buildSkillDto(Skill skill, UUID currentUserId) {
        User currentUser = userService.existUser(currentUserId);
        boolean isInMyDepartment = skill.getDepartments().stream()
                .anyMatch(department -> department.getIdDepartment().equals(currentUser.getIdDepartment()));

        return SkillDto.builder()
                .idSkill(skill.getIdSkill())
                .skillName(skill.getSkillName())
                .skillDescription(skill.getSkillDescription())
                .creatorName(userService.getSkilCreatorName(skill.getCreatedBy()))
                .skillCategoryName(skillCategoryService.getSkillCategoryById(skill.getIdSkillCategory()).getSkillCategoryName())
                .departments(departmentService.findDepartmentNamesBySkillAndOrganisation(skill.getIdSkill(), skill.getIdOrganisation()))
                .isInMyDepartment(isInMyDepartment)
                .build();
    }


    public List<SkillDto> getAllSkillsByOrganisation(UUID idOrganisation, UUID currentUserId) {
        List<Skill> skills = skillRepository.findByidOrganisation(idOrganisation);
        // Trebuie să trecem currentUserId la buildSkillDto pentru fiecare Skill în parte
        return skills.stream().map(skill -> buildSkillDto(skill, currentUserId)).collect(Collectors.toList());
    }


    public SkillDto getSkillById(UUID idSkill, UUID currentUserId) {
        Skill skill = skillRepository.findById(idSkill).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL));

        return buildSkillDto(skill, currentUserId);
    }


    public Skill addSkill(SkillDto skillDto){
        User user =  userService.existUser(skillDto.getCreatedBy());
        Department department = departmentService.getDepartmentById(user.getIdDepartment());

        Skill skill = Skill.builder()
                .skillName(skillDto.getSkillName())
                .idOrganisation(user.getIdOrganisation())
                .skillDescription(skillDto.getSkillDescription())
                .createdBy(skillDto.getCreatedBy())
                .idSkillCategory(skillDto.getIdSkillCategory())
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

    public Skill updateSkill (UUID idSkill, UUID idUser, SkillDto skillDto){
        Skill skill = skillRepository.findById(idSkill).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL));

        if (!skill.getCreatedBy().equals(idUser) ) {
            throw new FunctionalException(ApplicationConstants.ERROR_UPDATE_SKILL, HttpStatus.UNAUTHORIZED);
        }

        skill.setSkillName(skillDto.getSkillName());
        skill.setSkillDescription(skillDto.getSkillDescription());
        skill.setIdSkillCategory(skillDto.getIdSkillCategory());

        skillRepository.save(skill);
        return skill;
    }

    public void deleteSkill(UUID idSkill, UUID idUser){
        Skill skill = skillRepository.findById(idSkill).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL));

        if (!skill.getDepartments().isEmpty()) {
            throw new FunctionalException(ApplicationConstants.ERROR_SKILL_IN_USE_BY_DEPARTMENTS , HttpStatus.CONFLICT);
        }

        if (!skill.getCreatedBy().equals(idUser) ) {
            throw new FunctionalException(ApplicationConstants.ERROR_DELETE_SKILL, HttpStatus.UNAUTHORIZED);
        }

        skillRepository.deleteById(idSkill);
    }

}

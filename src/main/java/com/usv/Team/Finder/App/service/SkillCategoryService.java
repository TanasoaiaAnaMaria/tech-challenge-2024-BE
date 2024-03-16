package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.SkillCategoryDto;
import com.usv.Team.Finder.App.entity.SkillCategory;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.SkillCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillCategoryService {
    private final SkillCategoryRepository skillCategoryRepository;
    private final OrganisationService organisationService;
    private final UserService userService;

    public SkillCategoryService(SkillCategoryRepository skillCategoryRepository, OrganisationService organisationService, UserService userService) {
        this.skillCategoryRepository = skillCategoryRepository;
        this.organisationService = organisationService;
        this.userService = userService;
    }

    public List<SkillCategory> getSkillCategoryPerOrganisation(UUID idOrganisation) {
        List<SkillCategory> skilCategories = skillCategoryRepository.findByIdOrganisation(idOrganisation);
        if(skilCategories.isEmpty())
            throw new FunctionalException(ApplicationConstants.ERROR_MESSAGE_ORGANISATION, HttpStatus.NOT_FOUND);


        return skilCategories.stream()
                .map(skill -> SkillCategory.builder()
                        .idSkillCategory(skill.getIdSkillCategory())
                        .skillCategoryName(skill.getSkillCategoryName())
                        .idOrganisation(skill.getIdOrganisation())
                        .skills(skill.getSkills())
                        .build())
                .collect(Collectors.toList());
    }

    public SkillCategory getSkillCategoryById(UUID idSkillCategory){
        return skillCategoryRepository.findById(idSkillCategory).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL_CATEGORY));
    }

    public SkillCategory addSkillCategory(UUID idOrganisationAdmin, SkillCategoryDto skillCategoryDto){
        organisationService.getOrganisationById(skillCategoryDto.getIdOrganisation());

        User user = userService.existUser(idOrganisationAdmin);
        if(!user.getIdOrganisation().equals(skillCategoryDto.getIdOrganisation()) && user.getIsDepartmentManager())
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);

        SkillCategory skillCategory = SkillCategory.builder()
                .skillCategoryName(skillCategoryDto.getSkilCategoryName())
                .idOrganisation(skillCategoryDto.getIdOrganisation())
                .build();

        skillCategoryRepository.save(skillCategory);
        return skillCategory;
    }

    public SkillCategory updateSkillCategory(UUID idSkillCategory, SkillCategoryDto skillCategoryDto){
        SkillCategory existingSkillCategory = skillCategoryRepository.findById(idSkillCategory).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL_CATEGORY));

        if(!existingSkillCategory.getIdOrganisation().equals(skillCategoryDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);


        existingSkillCategory.setSkillCategoryName(skillCategoryDto.getSkilCategoryName());

        skillCategoryRepository.save(existingSkillCategory);
        return existingSkillCategory;
    }

    public void deleteSkillCategory(UUID idSkillCategory){
        if(skillCategoryRepository.findById(idSkillCategory).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKILL_CATEGORY);

        skillCategoryRepository.deleteById(idSkillCategory);
    }
}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.SkilCategoryDto;
import com.usv.Team.Finder.App.entity.SkilCategory;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.SkilCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkilCategoryService {
    private final SkilCategoryRepository skilCategoryRepository;
    private final OrganisationService organisationService;
    private final UserService userService;

    public SkilCategoryService(SkilCategoryRepository skilCategoryRepository, OrganisationService organisationService, UserService userService) {
        this.skilCategoryRepository = skilCategoryRepository;
        this.organisationService = organisationService;
        this.userService = userService;
    }

    public List<SkilCategory> getSkilCategoryPerOrganisation(UUID idOrganisation) {
        List<SkilCategory> skilCategories = skilCategoryRepository.findByIdOrganisation(idOrganisation);

        return skilCategories.stream()
                .map(skil -> SkilCategory.builder()
                        .idSkilCategory(skil.getIdSkilCategory())
                        .skilCategoryName(skil.getSkilCategoryName())
                        .idOrganisation(skil.getIdOrganisation())
                        .build())
                .collect(Collectors.toList());
    }

    public SkilCategory getSkilCategoryById(UUID idSkilCategory){
        return skilCategoryRepository.findById(idSkilCategory).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKIL_CATEGORY));
    }

    public SkilCategory addSkilCategory(UUID idOrganisationAdmin, SkilCategoryDto skilCategoryDto){
        organisationService.getOrganisationById(skilCategoryDto.getIdOrganisation());

        User user = userService.existUser(idOrganisationAdmin);
        if(!user.getIdOrganisation().equals(skilCategoryDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);

        SkilCategory skilCategory = SkilCategory.builder()
                .skilCategoryName(skilCategoryDto.getSkilCategoryName())
                .idOrganisation(skilCategoryDto.getIdOrganisation())
                .build();

        skilCategoryRepository.save(skilCategory);
        return skilCategory;
    }

    public SkilCategory updateSkilCategory(UUID idSkilCategory, SkilCategoryDto skilCategoryDto){
        SkilCategory existingSkilCategory = skilCategoryRepository.findById(idSkilCategory).orElseThrow(()->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKIL_CATEGORY));

        if(!existingSkilCategory.getIdOrganisation().equals(skilCategoryDto.getIdOrganisation()))
            throw new FunctionalException(ApplicationConstants.ERROR_NO_RIGHTS, HttpStatus.CONFLICT);


        existingSkilCategory.setSkilCategoryName(skilCategoryDto.getSkilCategoryName());

        skilCategoryRepository.save(existingSkilCategory);
        return existingSkilCategory;
    }

    public void deleteSkilCategory(UUID idSkilCategory){
        if(skilCategoryRepository.findById(idSkilCategory).isEmpty())
            throw new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_SKIL_CATEGORY);

        skilCategoryRepository.deleteById(idSkilCategory);
    }
}

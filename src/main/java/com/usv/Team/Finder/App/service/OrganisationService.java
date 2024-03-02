package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.repository.OrganisationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganisationService {
    public final OrganisationRepository organisationRepository;
    private final TokenService tokenService;

    public OrganisationService(OrganisationRepository organisationRepository, TokenService tokenService) {
        this.organisationRepository = organisationRepository;
        this.tokenService = tokenService;
    }

    public UUID addOrganisation(OrganisationDto organisation){
        Organisation organisation1=Organisation.builder()
                .organisationName(organisation.getOrganisationName())
                .headquarterAddress(organisation.getHeadquarterAddress())
                .build();

        Organisation organisationSaved = organisationRepository.save(organisation1);
        String baseUrl = "http://localhost:3000/register/employee/";
        String token = tokenService.generateEmployeeSignUpURL(organisationSaved.getIdOrganisation());
        organisationSaved.setRegistrationUrl(baseUrl+token);
        organisationRepository.save(organisationSaved);

        return organisationSaved.getIdOrganisation();
    }
}

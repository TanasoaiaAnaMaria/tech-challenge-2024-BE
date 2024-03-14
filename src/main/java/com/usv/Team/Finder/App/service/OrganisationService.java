package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
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

    public OrganisationDto getOrganisationById (UUID idOrganisation){
        Organisation organisation = organisationRepository.findById(idOrganisation)
                .orElseThrow(() -> new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_ORGANISATION));

        return OrganisationDto.builder()
                .organisationName(organisation.getOrganisationName())
                .headquarterAddress(organisation.getHeadquarterAddress())
                .registrationUrl(organisation.getRegistrationUrl())
                .build();
    }

    private void updateRegistrationUrl(Organisation organisation) {
        String baseUrl = "https://atc-2024-thepenguins-fe-linux-web-app.azurewebsites.net/register/employee";
        String token = tokenService.generateEmployeeSignUpURL(organisation.getIdOrganisation());
        organisation.setRegistrationUrl(baseUrl + token);
        organisationRepository.save(organisation);
    }

    public UUID addOrganisation(OrganisationDto organisationDto) {
        Organisation organisation = Organisation.builder()
                .organisationName(organisationDto.getOrganisationName())
                .headquarterAddress(organisationDto.getHeadquarterAddress())
                .build();

        Organisation organisationSaved = organisationRepository.save(organisation);
        updateRegistrationUrl(organisationSaved);

        return organisationSaved.getIdOrganisation();
    }

    public void updateOrganisationHeadquarterAddress(UUID idOrganisation, String newHeadquarterAddress) {
        Organisation organisation = organisationRepository.findById(idOrganisation)
                .orElseThrow(() -> new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_ORGANISATION));

        organisation.setHeadquarterAddress(newHeadquarterAddress);

        updateRegistrationUrl(organisation);
    }
}

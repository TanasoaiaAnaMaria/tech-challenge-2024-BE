package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.OrganisationRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.Deflater;

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

    private String encryptAndCompress(String data) throws Exception {
        byte[] input = data.getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[100];
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        int compressedDataLength = deflater.deflate(output);
        deflater.end();

        byte[] compressedData = new byte[compressedDataLength];
        System.arraycopy(output, 0, compressedData, 0, compressedDataLength);

        return Base64.getUrlEncoder().encodeToString(compressedData);
    }

    private void updateRegistrationUrl(Organisation organisation) throws Exception {
        String baseUrl = "https://atc-2024-thepenguins-fe-linux-web-app.azurewebsites.net/register/employee";
        String dataToEncrypt = organisation.getIdOrganisation() + ":" + organisation.getOrganisationName();
        String encryptedData = encryptAndCompress(dataToEncrypt);
        organisation.setRegistrationUrl(baseUrl +"/"+ encryptedData);
        organisationRepository.save(organisation);
    }

    public UUID addOrganisation(OrganisationDto organisationDto) throws Exception {
        Organisation organisation = Organisation.builder()
                .organisationName(organisationDto.getOrganisationName())
                .headquarterAddress(organisationDto.getHeadquarterAddress())
                .build();

        Organisation organisationSaved = organisationRepository.save(organisation);
        updateRegistrationUrl(organisationSaved);

        return organisationSaved.getIdOrganisation();
    }

    public void updateOrganisationHeadquarterAddress(UUID idOrganisation, String newHeadquarterAddress) throws Exception {
        Organisation organisation = organisationRepository.findById(idOrganisation)
                .orElseThrow(() -> new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_ORGANISATION));

        organisation.setHeadquarterAddress(newHeadquarterAddress);

        updateRegistrationUrl(organisation);
    }
}

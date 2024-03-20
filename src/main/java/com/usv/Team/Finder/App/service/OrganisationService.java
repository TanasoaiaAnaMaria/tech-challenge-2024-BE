package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.DepartmentDto;
import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.dto.OrganisationStatisticsDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Organisation;
import com.usv.Team.Finder.App.entity.Project;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.zip.Deflater;

@Service
public class OrganisationService {
    public final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

    public OrganisationService(OrganisationRepository organisationRepository, UserRepository userRepository,DepartmentRepository departmentRepository, ProjectRepository projectRepository) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.projectRepository = projectRepository;
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

    public OrganisationStatisticsDto getOrganisationStatistics(UUID organisationId) {
        OrganisationStatisticsDto stats = new OrganisationStatisticsDto();

        stats.setNumberOfEmployees(0);
        stats.setNumberOfProjectManagers(0);
        stats.setNumberOfDepartmentManagers(0);
        stats.setNumberOfOrganisationAdmins(0);
        stats.setNumberOfDepartments(0);
        stats.setNumberOfProjects(0);

        List<User> users = userRepository.findByIdOrganisation(organisationId);
        if (users != null && !users.isEmpty()) {
            stats.setNumberOfEmployees((int) users.stream()
                    .filter(user -> user.getAuthorities().stream()
                            .anyMatch(role -> "EMPLOYEE".equals(role.getAuthority())))
                    .count());
            stats.setNumberOfProjectManagers((int) users.stream()
                    .filter(user -> user.getAuthorities().stream()
                            .anyMatch(role -> "PROJECT_MANAGER".equals(role.getAuthority())))
                    .count());
            stats.setNumberOfDepartmentManagers((int) users.stream()
                    .filter(user -> user.getAuthorities().stream()
                            .anyMatch(role -> "DEPARTMENT_MANAGER".equals(role.getAuthority())))
                    .count());
            stats.setNumberOfOrganisationAdmins((int) users.stream()
                    .filter(user -> user.getAuthorities().stream()
                            .anyMatch(role -> "ORGANISATION_ADMIN".equals(role.getAuthority())))
                    .count());
        }

        List<Department> departments = departmentRepository.findByIdOrganisation(organisationId);
        if (departments != null && !departments.isEmpty()) {
            stats.setNumberOfDepartments(departments.size());
        }

        List<Project> projects = projectRepository.findByIdOrganisation(organisationId);
        if (projects != null && !projects.isEmpty()) {
            stats.setNumberOfProjects(projects.size());
        }

        return stats;
    }


}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OrganisationService organisationService;

    private final DepartmentService departmentService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, OrganisationService organisationService, DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.organisationService = organisationService;
        this.departmentService = departmentService;
    }

    @Override
    public UserDetails loadUserByUsername(String eMailAdress) throws UsernameNotFoundException {
        return userRepository.findByeMailAdress(eMailAdress).orElseThrow(()-> new UsernameNotFoundException("User not found :("));
    }
    private List<String> getOrganisationAdminNames(UUID departmentId) {
        UUID idOrganisation = departmentService.getDepartmentById(departmentId).getIdOrganisation();

        List<User> usersInOrganisation = userRepository.findByIdOrganisation(idOrganisation);

        return usersInOrganisation.stream()
                .filter(user -> user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ORGANISATION_ADMIN")))
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .collect(Collectors.toList());
    }

    public UUID getDepartmentManagerId(UUID departmentId) {
        return userRepository.findByIdDepartmentAndIsDepartmentManager(departmentId, true)
                .map(User::getIdUser) // Convertim Optional<User> în Optional<UUID>
                .orElse(null); // Returnăm null dacă nu se găsește niciun rezultat
    }

    public List<UserDto> getUsersPerOrganisation(UUID idOrganisation){
        List<User> userList = userRepository.findByIdOrganisation(idOrganisation);
        List<UserDto> users = new ArrayList<>();

        userList.forEach(user -> {
            String departmentName = null;
            List<String> organisationAdminNames = null;
            if (user.getIdDepartment() != null) {
                departmentName = departmentService.getDepartmentById(user.getIdDepartment()).getDepartmentName();
                organisationAdminNames = getOrganisationAdminNames(user.getIdOrganisation());
            }

            String departmentManagerName = null;
            UUID departmentManagerId = getDepartmentManagerId(user.getIdDepartment());

            if (!user.isDepartmentManager() && departmentManagerId != null) {
                UserDto manager = getUserById(departmentManagerId);
                departmentManagerName = manager.getFirstName() + " " + manager.getLastName();
            }

            users.add(UserDto.builder()
                    .idUser(user.getIdUser())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .eMailAdress(user.getEMailAdress())
                    .idOrganisation(user.getIdOrganisation())
                    .organisationName(organisationService.getOrganisationById(user.getIdOrganisation()).getOrganisationName())
                    .authorities(user.getAuthorities())
                    .isDepartmentManager(user.isDepartmentManager())
                    .departmentManagerName(departmentManagerName)
                    .idDepartment(user.getIdDepartment())
                    .departmentName(departmentName)
                    .OrganisationAdminNames(organisationAdminNames)
                    .build());
        });

        return users;
    }

    public UserDto getUserById(UUID idUser){
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));

        String departmentName = null;
        List<String> organisationAdminNames = null;
        if (user.getIdDepartment() != null) {
            departmentName = departmentService.getDepartmentById(user.getIdDepartment()).getDepartmentName();
            organisationAdminNames = getOrganisationAdminNames(user.getIdOrganisation());
        }

        String departmentManagerName = null;
        UUID departmentManagerId = getDepartmentManagerId(user.getIdDepartment());

        if (!user.isDepartmentManager() && departmentManagerId != null) {
            UserDto manager = getUserById(departmentManagerId);
            departmentManagerName = manager.getFirstName() + " " + manager.getLastName();
        }

        return UserDto.builder()
                .idUser(user.getIdUser())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .eMailAdress(user.getEMailAdress())
                .idOrganisation(user.getIdOrganisation())
                .organisationName(organisationService.getOrganisationById(user.getIdOrganisation()).getOrganisationName())
                .authorities(user.getAuthorities())
                .isDepartmentManager(user.isDepartmentManager())
                .departmentManagerName(departmentManagerName)
                .idDepartment(user.getIdDepartment())
                .departmentName(departmentName)
                .OrganisationAdminNames(organisationAdminNames)
                .build();
    }
}
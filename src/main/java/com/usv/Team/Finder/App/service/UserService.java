package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OrganisationService organisationService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, OrganisationService organisationService, DepartmentService departmentService, RoleService roleService) {
        this.userRepository = userRepository;
        this.organisationService = organisationService;
        this.departmentService = departmentService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String eMailAdress) throws UsernameNotFoundException {
        return userRepository.findByeMailAdress(eMailAdress).orElseThrow(()-> new UsernameNotFoundException("User not found :("));
    }

    public List<UserDto> getUsersPerOrganisation(UUID idOrganisation){
        List<User> userList = userRepository.findByIdOrganisation(idOrganisation);
        List<UserDto> users = new ArrayList<>();

        userList.forEach(user -> {
            List<String> organisationAdminNames = null;
            organisationAdminNames = getOrganisationAdminNames(user.getIdOrganisation());
            String departmentName = getDepartmentName(user.getIdDepartment());
            String departmentManagerName = getDepartmentManagerName(user.getIdDepartment());

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

        List<String> organisationAdminNames = null;
        organisationAdminNames = getOrganisationAdminNames(user.getIdOrganisation());
        String departmentName = getDepartmentName(user.getIdDepartment());
        String departmentManagerName = getDepartmentManagerName(user.getIdDepartment());

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

    public UserDto addUserRole(UUID idUser, Long idRole) {
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));

        Role roleToAdd = roleService.getRoleById(idRole);
        if (user.getAuthorities().size() == 1 && user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("EMPLOYEE"))) {
            user.setAuthorities(new HashSet<>(Arrays.asList(roleToAdd)));
        } else {
            user.getAuthorities().add(roleToAdd);
        }

        userRepository.save(user);

        return getUserById(user.getIdUser());
    }

    public UserDto removeUserRole(UUID idUser, Long idRole) {
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));

        Role roleToRemove = roleService.getRoleById(idRole);
        Role employeeRole = roleService.getRoleByAuthority("EMPLOYEE");

        boolean removed = user.getAuthorities().removeIf(role -> role.getId().equals(roleToRemove.getId()));

        if (removed && user.getAuthorities().isEmpty()) {
            user.getAuthorities().add(employeeRole);
        } else if (!removed) {
            // Rolul specificat nu a fost găsit la utilizator.
            throw new CrudOperationException("Specified role not found for user.");
        }

        userRepository.save(user);

        return getUserById(user.getIdUser());
    }

    public List<UserDto> getUnassignedDepartmentManagers() {
         List<User> allUsers = (List<User>) userRepository.findAll();
        List<UserDto> unassignedManagers = allUsers.stream()
                .filter(user -> user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("DEPARTMENT_MANAGER")) && user.getIdDepartment() == null)
                .map(user -> getUserById(user.getIdUser())) // Apelăm getUserById pentru fiecare user filtrat
                .collect(Collectors.toList());

        return unassignedManagers;
    }

    private List<String> getOrganisationAdminNames(UUID idOrganisation) {
        List<User> usersInOrganisation = userRepository.findByIdOrganisation(idOrganisation);

        List<String> organisationAdminNames = new ArrayList<>();

        for (User user : usersInOrganisation) {
            for (Role authority : user.getAuthorities()) {
                if ("ORGANIZATION_ADMIN".equals(authority.getAuthority())) {
                    String fullName = user.getFirstName() + " " + user.getLastName();
                    organisationAdminNames.add(fullName);
                }
            }
        }

        return organisationAdminNames;
    }

    public UUID getDepartmentManagerId(UUID departmentId) {
        return userRepository.findByIdDepartmentAndIsDepartmentManager(departmentId, true)
                .map(User::getIdUser)
                .orElse(null);
    }

    private String getDepartmentName(UUID departmentId) {
        if (departmentId != null) {
            return departmentService.getDepartmentById(departmentId).getDepartmentName();
        }
        return null;
    }

    private String getDepartmentManagerName(UUID departmentId) {
        if (departmentId != null) {
            UUID departmentManagerId = getDepartmentManagerId(departmentId);
            if (departmentManagerId != null) {
                UserDto manager = getUserById(departmentManagerId);
                return manager.getFirstName() + " " + manager.getLastName();
            }
        }
        return null;
    }


}
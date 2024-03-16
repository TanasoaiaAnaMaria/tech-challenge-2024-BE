package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.OrganisationDto;
import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.CrudOperationException;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OrganisationService organisationService;
    private final DepartmentService departmentService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, OrganisationService organisationService, DepartmentService departmentService, RoleService roleService) {
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
        if(userList.isEmpty())
                throw new CrudOperationException(ApplicationConstants.ERROR_USERS_FROM_ORGANISATION);
        List<UserDto> users = new ArrayList<>();

        userList.forEach(user -> {
            List<String> organisationAdminNames = null;
            organisationAdminNames = getOrganisationAdminNames(user.getIdOrganisation());
            String departmentName = getDepartmentName(user.getIdDepartment());
            String departmentManagerName = getDepartmentManagerName(user.getIdDepartment());

            OrganisationDto organisation =  organisationService.getOrganisationById(user.getIdOrganisation());

            users.add(UserDto.builder()
                    .idUser(user.getIdUser())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .eMailAdress(user.getEMailAdress())
                    .idOrganisation(user.getIdOrganisation())
                    .organisationName(organisation.getOrganisationName())
                    .organisationHeadquarterAddress(organisation.getHeadquarterAddress())
                    .authorities(user.getAuthorities())
                    .isDepartmentManager(user.getIsDepartmentManager())
                    .departmentManagerName(departmentManagerName)
                    .idDepartment(user.getIdDepartment())
                    .departmentName(departmentName)
                    .OrganisationAdminNames(organisationAdminNames)
                    .skilsCreated(user.getSkilsCreated())
                    .build());
        });

        return users;
    }

    public UserDto getUserById(UUID idUser){
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));
        OrganisationDto organisation = organisationService.getOrganisationById(user.getIdOrganisation());

        List<String> organisationAdminNames = null;
        organisationAdminNames = getOrganisationAdminNames(user.getIdOrganisation());
        String departmentName = getDepartmentName(user.getIdDepartment());
        String departmentManagerName = getDepartmentManagerName(user.getIdDepartment());

        String registrationUrl = null;
        for (Role role : user.getAuthorities()) {
            if ("ORGANISATION_ADMIN".equals(role.getAuthority())) {
                registrationUrl = organisation.getRegistrationUrl();
                break;
            }
        }

        return UserDto.builder()
                .idUser(user.getIdUser())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .eMailAdress(user.getEMailAdress())
                .idOrganisation(user.getIdOrganisation())
                .organisationName(organisation.getOrganisationName())
                .organisationHeadquarterAddress(organisation.getHeadquarterAddress())
                .authorities(user.getAuthorities())
                .isDepartmentManager(user.getIsDepartmentManager())
                .departmentManagerName(departmentManagerName)
                .idDepartment(user.getIdDepartment())
                .departmentName(departmentName)
                .OrganisationAdminNames(organisationAdminNames)
                .registrationUrl(registrationUrl)
                .skilsCreated(user.getSkilsCreated())
                .build();
    }

    public User existUser(UUID idUser){
        return userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));
    }

    public List<User> getUsersPerDepartment(UUID idDepartment) {
        if (idDepartment == null) {
            throw new FunctionalException(ApplicationConstants.ERROR_NULL_PARAMETER, HttpStatus.BAD_REQUEST);
        }

        departmentService.getDepartmentById(idDepartment);
        List<User> usersInDepartment = userRepository.findByIdDepartment(idDepartment);

        return usersInDepartment.stream()
                .map(user -> existUser(user.getIdUser()))
                .collect(Collectors.toList());
    }

    public void addUserRole(UUID idUser, Long idRole) {
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));

        Role roleToAdd = roleService.getRoleById(idRole);
        if (user.getAuthorities().size() == 1 && user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("EMPLOYEE"))) {
            user.setAuthorities(new HashSet<>(Collections.singletonList(roleToAdd)));
        } else {
            user.getAuthorities().add(roleToAdd);
        }

        userRepository.save(user);

        getUserById(user.getIdUser());
    }

    public void removeUserRole(UUID idUser, Long idRole) {
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new CrudOperationException(ApplicationConstants.ERROR_MESSAGE_USER));

        Role roleToRemove = roleService.getRoleById(idRole);
        Role employeeRole = roleService.getRoleByAuthority("EMPLOYEE");

        boolean removed = user.getAuthorities().removeIf(role -> role.getId().equals(roleToRemove.getId()));

        if (removed && user.getAuthorities().isEmpty()) {
            user.getAuthorities().add(employeeRole);
        } else if (!removed) {
            throw new CrudOperationException(ApplicationConstants.ERROR_ROLE_NOT_FOUND_FOR_USER);
        }

        userRepository.save(user);

        getUserById(user.getIdUser());
    }

    public List<UserDto> getUnassignedDepartmentManagers() {
         List<User> allUsers = (List<User>) userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("DEPARTMENT_MANAGER")) && user.getIdDepartment() == null)
                .map(user -> getUserById(user.getIdUser())) // Apelăm getUserById pentru fiecare user filtrat
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersWithoutDepartment() {
        List<User> allUsers = (List<User>) userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user.getIdDepartment() == null)
                .map(user -> getUserById(user.getIdUser()))
                .collect(Collectors.toList());
    }

    public void assignUserToDepartment(UUID userId, UUID idDepartment) {
        Department department = departmentService.getDepartmentById(idDepartment);

        User employee = existUser(userId);

        if(employee.getIdDepartment()==null){
            employee.setIdDepartment(department.getIdDepartment());
            userRepository.save(employee);
        }
        else
            throw new CrudOperationException(ApplicationConstants.ERROR_USER_ALREADY_ASSIGNED_TO_A_DEPARTMENT);
    }

    public void removeUserFromDepartment(UUID userId) {
        User employee = existUser(userId);
        employee.setIdDepartment(null);
        userRepository.save(employee);
    }

    public void addDepartmentManager(UUID idUser, UUID idDepartment) {
        Department department = departmentService.getDepartmentById(idDepartment);
        UUID existingManagerId = department.getDepartmentManager();
        if (existingManagerId != null) {
            User previousManager = existUser(existingManagerId);
            deasignDepartmentManager(previousManager);
        }

        User newManager = existUser(idUser);
        asignDepartmentManager(newManager, department);

        departmentService.updateDepartmentManager(department, newManager.getIdUser());
    }

    public void removeDepartmentManagerFromDepartment(UUID userId) {
        User employee = existUser(userId);
        employee.setIdDepartment(null);
        employee.setIsDepartmentManager(false);
        userRepository.save(employee);
    }

    //////////////////////////////////////////////////////////////////////////////////////

    public void deasignDepartmentManager(User user){
        user.setIsDepartmentManager(false);
        user.setIdDepartment(null);

        userRepository.save(user);
    }

    public void asignDepartmentManager(User user, Department department){
        user.setIsDepartmentManager(true);
        user.setIdDepartment(department.getIdDepartment());
        userRepository.save(user);
    }

    private List<String> getOrganisationAdminNames(UUID idOrganisation) {
        List<User> usersInOrganisation = userRepository.findByIdOrganisation(idOrganisation);

        List<String> organisationAdminNames = new ArrayList<>();

        for (User user : usersInOrganisation) {
            for (Role authority : user.getAuthorities()) {
                if ("ORGANISATION_ADMIN".equals(authority.getAuthority())) {
                    String fullName = user.getFirstName() + " " + user.getLastName();
                    organisationAdminNames.add(fullName);
                }
            }
        }

        return organisationAdminNames;
    }

    public String getSkilCreatorName(UUID idSkilCreator) {
        User user = existUser(idSkilCreator);
        return user.getFirstName() + " " + user.getLastName();
    }


    private String getDepartmentName(UUID departmentId) {
        if (departmentId != null) {
            return departmentService.getDepartmentById(departmentId).getDepartmentName();
        }
        return null;
    }

    private String getDepartmentManagerName(UUID departmentId) {
        if (departmentId != null) {
            Department department = departmentService.getDepartmentById(departmentId);
            UUID departmentManagerId = department.getDepartmentManager();
            if (departmentManagerId != null) {
                User manager = existUser(departmentManagerId);
                return manager.getFirstName() + " " + manager.getLastName();
            }
        }
        return null;
    }


}
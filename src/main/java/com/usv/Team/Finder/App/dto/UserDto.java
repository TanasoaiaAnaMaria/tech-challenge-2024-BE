package com.usv.Team.Finder.App.dto;

import com.usv.Team.Finder.App.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    private UUID idUser;

    private String firstName;

    private String lastName;

    private String eMailAdress;

    private String password;

    private UUID idOrganisation;

    private String organisationName;

    private String organisationHeadquarterAddress;

    private Set<Role> authorities;

    private boolean isDepartmentManager;

    private String departmentManagerName;

    private UUID idDepartment;

    private String departmentName;

    private List<String> OrganisationAdminNames;

    private String registrationUrl;
}
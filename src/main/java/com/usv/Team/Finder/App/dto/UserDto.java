package com.usv.Team.Finder.App.dto;

import com.usv.Team.Finder.App.entity.Role;
import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.entity.User_Skill;
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

    private Boolean isDepartmentManager;

    private String departmentManagerName;

    private UUID idDepartment;

    private String departmentName;

    private List<String> OrganisationAdminNames;

    private String registrationUrl;

    private Set<Skill> skilsCreated;

    private Set<User_Skill> userSkill;
}
package com.usv.Team.Finder.App.dto;

import com.usv.Team.Finder.App.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class RegisterOrganisationAdminDto {
    private String firstName;

    private String lastName;

    private String eMailAdress;

    private String password;

    private Set<Role> authorities;

    private String organisationName;

    private String headquarterAddress;
}

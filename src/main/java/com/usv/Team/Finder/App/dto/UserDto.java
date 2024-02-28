package com.usv.Team.Finder.App.dto;

import com.usv.Team.Finder.App.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    private String firstName;

    private String lastName;

    private String eMailAdress;

    private String password;

    private UUID idOrganisation;

    private Set<Role> authorities;
}
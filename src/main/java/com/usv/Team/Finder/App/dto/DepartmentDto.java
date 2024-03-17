package com.usv.Team.Finder.App.dto;

import com.usv.Team.Finder.App.entity.Skill;
import com.usv.Team.Finder.App.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class DepartmentDto {
    UUID idDepartment;

    UUID idOrganisation;

    String departmentName;

    UUID departmentManager;

    String departmentManagerName;

    private Set<User> users;

    private Set<Skill> skills;
}

package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class DepartmentDto {
    UUID idOrganisation;

    String departmentName;

    UUID departmentManager;
}

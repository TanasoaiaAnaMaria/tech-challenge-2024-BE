package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationStatisticsDto {
    private long numberOfEmployees;
    private long numberOfProjectManagers;
    private long numberOfDepartmentManagers;
    private long numberOfOrganisationAdmins;
    private long numberOfDepartments;
    private long numberOfProjects;

}

package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OrganisationDto {
    private String organisationName;

    private String headquarterAddress;
}
package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private String projectName;

    private String projectPeriod;

    private LocalDateTime startDate;

    private LocalDateTime deadlineDate;

    private String generalDescription;

    private UUID idOrganisation;

    private UUID createdBy;

    private String projectStatus;

    private Boolean canBeDeleted;

    private List<Project_TeamRoleDto> teamRoles;
}

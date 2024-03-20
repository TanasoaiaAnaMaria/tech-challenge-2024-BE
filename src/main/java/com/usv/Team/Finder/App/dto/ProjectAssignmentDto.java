package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAssignmentDto {
    private UUID idProject;

    private UUID idTeamRole;

    private UUID idUser;

    private int workHours;

    private boolean active;
}

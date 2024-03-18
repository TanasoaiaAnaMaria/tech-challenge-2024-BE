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
public class Project_TeamRoleDto {
    private UUID idProiect;

    private String projectName;

    private UUID idTeamRole;

    private String teamRoleName;

    private Long numberOfMembers;
}

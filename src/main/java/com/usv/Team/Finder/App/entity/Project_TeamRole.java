package com.usv.Team.Finder.App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "project_teamRole")
public class Project_TeamRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idProject_TeamRole;

    private UUID idProiect;

    private String projectName;

    private UUID idTeamRole;

    private String teamRoleName;

    private Long numberOfMembers;
}

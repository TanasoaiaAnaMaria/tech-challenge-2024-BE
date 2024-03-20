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
@Table(name = "projectAssignment")
public class ProjectAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idProjectAssignment;

    private UUID idProject;

    private String projectName;

    private UUID idTeamRole;

    private String teamRoleName;

    private UUID idUser;

    private int workHours;

    private boolean active;
}

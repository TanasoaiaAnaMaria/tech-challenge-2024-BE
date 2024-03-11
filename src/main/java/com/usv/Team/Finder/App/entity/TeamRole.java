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
@Table(name = "teamRole")
public class TeamRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTeamRole;

    String teamRoleName;

    UUID idOrganisation;
}

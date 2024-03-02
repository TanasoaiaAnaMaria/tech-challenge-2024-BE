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
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID idOrganisationAdmin;

    private UUID idOrganisation;

    private String emailEmployee;

    private boolean registered = false;
}

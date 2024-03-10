package com.usv.Team.Finder.App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    private LocalDateTime sentDate;

    private boolean registered = false;

    private boolean expired = false;
}

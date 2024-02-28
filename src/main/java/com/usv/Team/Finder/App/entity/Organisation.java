package com.usv.Team.Finder.App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "organisations")
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idOrganisation;

    private String organisationName;

    private String headquarterAddress;

    private String registrationUrl;

    @OneToMany(
            targetEntity = User.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="idOrganisation", referencedColumnName = "idOrganisation")
    private Set<User> users;
}

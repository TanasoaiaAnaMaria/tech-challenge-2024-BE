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
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idDepartment;

    UUID idOrganisation;

    String departmentName;

    UUID departmentManager;

    @OneToMany(
            targetEntity = User.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="idDepartment", referencedColumnName = "idDepartment")
    private Set<User> users;

}

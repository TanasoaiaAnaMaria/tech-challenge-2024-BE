package com.usv.Team.Finder.App.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idProject;

    private String projectName;

    private String projectPeriod;

    private LocalDateTime startDate;

    private LocalDateTime deadlineDate;

    private String generalDescription;

    private UUID idOrganisation;

    private UUID createdBy;

    private String projectStatus;

    private Boolean canBeDeleted;

    @OneToMany(
            targetEntity = Project_TeamRole.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="idProiect", referencedColumnName = "idProject")
    private List<Project_TeamRole> teamRoles;
}

package com.usv.Team.Finder.App.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "Skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSkill;

    private String skillName;

    private UUID idOrganisation;

    private String skillDescription;

    private UUID createdBy;

    private UUID idSkillCategory;

    @JsonIgnore
    @ManyToMany(mappedBy = "skills")
    private Set<Department> departments= new HashSet<>();
}

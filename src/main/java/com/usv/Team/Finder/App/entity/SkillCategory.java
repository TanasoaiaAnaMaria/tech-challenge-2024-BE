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
@Table(name = "skillCategory")
public class SkillCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSkillCategory;

    private String skillCategoryName;

    UUID idOrganisation;

    @OneToMany(
            targetEntity = Skill.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="idSkillCategory", referencedColumnName = "idSkillCategory")
    private Set<Skill> skills;
}

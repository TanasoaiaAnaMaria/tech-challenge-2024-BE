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
@Table(name = "skilCategory")
public class SkillCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSkilCategory;

    private String skilCategoryName;

    UUID idOrganisation;

    @OneToMany(
            targetEntity = Skill.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name="idSkilCategory", referencedColumnName = "idSkilCategory")
    private Set<User> skilCategorys;
}

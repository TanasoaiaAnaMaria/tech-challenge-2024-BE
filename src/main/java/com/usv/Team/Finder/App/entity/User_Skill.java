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
@Table(name = "user_skill")
public class User_Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUserSkill;

    @Column(name = "idUser")
    private UUID idUser;

    private String numeUser;

    @Column(name = "idSkill")
    private UUID idSkill;

    private String numeSkill;

    private String level;

    private String experience;

    private boolean approved;
}

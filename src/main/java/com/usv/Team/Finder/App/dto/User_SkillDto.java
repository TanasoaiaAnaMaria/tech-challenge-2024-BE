package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User_SkillDto {
    private UUID idUserSkill;

    private UUID idUser;
    private UUID idSkill;
    private String level;
    private String experience;
    private boolean approved;

}

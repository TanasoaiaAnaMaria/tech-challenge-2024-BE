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
public class SkillCategoryDto {
    private String skilCategoryName;
    UUID idOrganisation;
}

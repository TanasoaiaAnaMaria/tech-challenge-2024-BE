package com.usv.Team.Finder.App.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@Getter
@Setter
public class SkillDto {
    private UUID idSkill;

    private String skillName;

    private String skillDescription;

    private UUID createdBy;

    private String creatorName;

    private UUID idSkillCategory;

    private String skillCategoryName;

    private List<String> departments;

    // if this fild is true the skill will be automaticaly added to the creator department
    private Boolean adToMyDepartment;

    private boolean isInMyDepartment;

    private Boolean editable;
}

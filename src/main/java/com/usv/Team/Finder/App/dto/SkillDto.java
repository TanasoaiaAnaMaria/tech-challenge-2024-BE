package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class SkillDto {
    private String skilName;

    private String skilDescription;

    private UUID createdBy;

    private UUID idSkilCategory;

    // if this fild is true the skill will be automaticaly added to the creator department
    private Boolean adToMyDepartment;

    private Boolean editable;
}

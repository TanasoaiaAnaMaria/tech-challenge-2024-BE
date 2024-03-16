package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.SkillCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillCategoryRepository extends CrudRepository<SkillCategory, UUID> {
    List<SkillCategory> findByIdOrganisation(UUID idOrganisation);
}

package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<Skill, UUID> {
    List<Skill> findByidOrganisation(UUID idOrganisation);
    List<Skill> findByidSkillCategory(UUID idSkillCategory);
    List<Skill> findByCreatedBy(UUID createdBy);


}

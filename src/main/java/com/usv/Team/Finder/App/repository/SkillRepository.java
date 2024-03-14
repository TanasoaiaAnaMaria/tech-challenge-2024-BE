package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<Skill, UUID> {
}

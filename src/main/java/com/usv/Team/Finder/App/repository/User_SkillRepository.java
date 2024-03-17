package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.User_Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface User_SkillRepository extends CrudRepository<User_Skill, UUID> {
    List<User_Skill> findByIdUser(UUID idUser);
    List<User_Skill> findByIdUserAndApproved(UUID idUser, boolean approved);
}

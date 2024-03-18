package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Project_TeamRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Project_TeamRoleRepository  extends CrudRepository<Project_TeamRole, UUID> {
}

package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository  extends CrudRepository<Project, UUID> {
}

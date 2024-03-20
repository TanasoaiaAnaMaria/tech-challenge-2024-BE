package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.ProjectAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectAssignmentRepository extends CrudRepository<ProjectAssignment, UUID> {
    List<ProjectAssignment> findByIdProject(UUID idProject);
    List<ProjectAssignment> findByIdUserAndActive(UUID idUser, boolean active);
}

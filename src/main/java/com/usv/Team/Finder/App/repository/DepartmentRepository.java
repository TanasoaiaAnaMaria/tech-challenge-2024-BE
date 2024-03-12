package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Department;
import com.usv.Team.Finder.App.entity.TeamRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, UUID> {
    List<Department> findByIdOrganisation(UUID idOrganisation);

}

package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Organisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, UUID> {
}

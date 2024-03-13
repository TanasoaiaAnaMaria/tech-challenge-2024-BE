package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.SkilCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkilCategoryRepository extends CrudRepository<SkilCategory, UUID> {
    List<SkilCategory> findByIdOrganisation(UUID idOrganisation);
}

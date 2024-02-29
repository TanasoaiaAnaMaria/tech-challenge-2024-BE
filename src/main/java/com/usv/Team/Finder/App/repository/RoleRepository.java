package com.usv.Team.Finder.App.repository;


import com.usv.Team.Finder.App.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {
    Optional<Role> findByAuthority(String authority);
}
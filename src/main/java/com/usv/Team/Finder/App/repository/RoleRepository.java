package com.usv.Team.Finder.App.repository;


import com.usv.Team.Finder.App.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByAuthority(String authority);
}
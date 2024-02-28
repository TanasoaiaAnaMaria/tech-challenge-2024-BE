package com.usv.Team.Finder.App.repository;


import com.usv.Team.Finder.App.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByeMailAdress(String eMailAdress);
}
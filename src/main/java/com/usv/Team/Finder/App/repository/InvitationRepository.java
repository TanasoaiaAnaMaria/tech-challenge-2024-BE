package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Invitation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface InvitationRepository extends CrudRepository<Invitation, UUID> {
    List<Invitation> findAllByIdOrganisation(UUID idOrganisation);
}

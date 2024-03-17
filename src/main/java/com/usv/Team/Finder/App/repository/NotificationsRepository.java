package com.usv.Team.Finder.App.repository;

import com.usv.Team.Finder.App.entity.Notifications;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface NotificationsRepository extends CrudRepository<Notifications, UUID> {
    List<Notifications> findByAddressedTo(UUID userId);
}

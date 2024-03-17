package com.usv.Team.Finder.App.controller;

import com.usv.Team.Finder.App.dto.NotificationsDto;
import com.usv.Team.Finder.App.entity.Notifications;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.service.NotificationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@CrossOrigin("*")
public class NotificationsController {
    private final NotificationsService notificationsService;

    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping("/getByAddressedTo")
    @PreAuthorize("hasAnyRole('DEPARTMENT_MANAGER','EMPLOYEE')")
    public ResponseEntity<List<NotificationsDto>> getNotificationsByAddressedTo(@RequestParam UUID idUser) {
        try {
            List<NotificationsDto> notifications = notificationsService.getNotificationsByAddressedTo(idUser);
            return ResponseEntity.ok(notifications);
        } catch (FunctionalException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getById")
    @PreAuthorize("hasAnyRole('DEPARTMENT_MANAGER','EMPLOYEE')")
    public ResponseEntity<NotificationsDto> getNotificationById(@RequestParam UUID idNotification) {
        try {
            NotificationsDto notification = notificationsService.getNotificationById(idNotification);
            return ResponseEntity.ok(notification);
        } catch (FunctionalException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<Notifications> addNotification(@RequestBody NotificationsDto notificationsDto) {
        Notifications notification = notificationsService.addNotification(notificationsDto);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('DEPARTMENT_MANAGER')")
    public ResponseEntity<?> deleteNotification(@RequestParam UUID idNotification) {
        try {
            notificationsService.deleteNotification(idNotification);
            return ResponseEntity.ok().build();
        } catch (FunctionalException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

package com.usv.Team.Finder.App.service;

import com.usv.Team.Finder.App.dto.NotificationsDto;
import com.usv.Team.Finder.App.dto.UserDto;
import com.usv.Team.Finder.App.entity.Notifications;
import com.usv.Team.Finder.App.entity.User;
import com.usv.Team.Finder.App.exception.FunctionalException;
import com.usv.Team.Finder.App.repository.ApplicationConstants;
import com.usv.Team.Finder.App.repository.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationsService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final NotificationsRepository notificationsRepository;
    private final UserService userService;

    public NotificationsService(NotificationsRepository notificationsRepository, UserService userService) {
        this.notificationsRepository = notificationsRepository;
        this.userService = userService;
    }

    public void sendNotificationToAllUsers(Object notificationMessage) {
        messagingTemplate.convertAndSend("/topic/notifications", notificationMessage);
    }

    public List<NotificationsDto> getNotificationsByAddressedTo(UUID idUser) {
        List<Notifications> notifications = notificationsRepository.findByAddressedTo(idUser);
        if (notifications.isEmpty()) {
            throw new FunctionalException(ApplicationConstants.ERROR_MESSAGE_NO_NOTIFICATIONS, HttpStatus.NOT_FOUND);
        }

        return notifications.stream()
                .map(notification -> NotificationsDto.builder()
                        .idNotification(notification.getIdNotifications())
                        .title(notification.getTitle())
                        .type(notification.getType())
                        .description(notification.getDescription())
                        .addressedTo(notification.getAddressedTo())
                        .createdAt(notification.getCreatedAt())
                        .open(notification.getOpen())
                        .build())
                .collect(Collectors.toList());
    }

    public NotificationsDto getNotificationById(UUID idNotification) {
        Notifications notification = notificationsRepository.findById(idNotification)
                .orElseThrow(() -> new FunctionalException(ApplicationConstants.ERROR_MESSAGE_NOTIFICATION_NOT_FOUND, HttpStatus.NOT_FOUND));

        return NotificationsDto.builder()
                .idNotification(notification.getIdNotifications())
                .title(notification.getTitle())
                .type(notification.getType())
                .description(notification.getDescription())
                .addressedTo(notification.getAddressedTo())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public Notifications addNotification(NotificationsDto notificationsDto) {
        Notifications notification = Notifications.builder()
                .title(notificationsDto.getTitle())
                .type(notificationsDto.getType())
                .description(notificationsDto.getDescription())
                .addressedTo(notificationsDto.getAddressedTo())
                .createdAt(notificationsDto.getCreatedAt())
                .build();
        notificationsRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notificationsDto);
        return notification;
    }

    public void deleteNotification(UUID idNotification){
        notificationsRepository.findById(idNotification)
                .orElseThrow(() -> new FunctionalException(ApplicationConstants.ERROR_MESSAGE_NOTIFICATION_NOT_FOUND, HttpStatus.NOT_FOUND));

        notificationsRepository.deleteById(idNotification);
    }



}

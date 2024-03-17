package com.usv.Team.Finder.App.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idNotifications;

    private String title;

    private String type;

    private String description;

    private UUID addressedTo;

    private LocalDateTime createdAt;

    private Boolean open;

}

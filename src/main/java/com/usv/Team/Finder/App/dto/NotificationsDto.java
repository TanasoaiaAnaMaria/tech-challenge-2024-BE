package com.usv.Team.Finder.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsDto {
    private UUID idNotification;
    private String title;
    private String type;
    private String description;
    private UUID addressedTo;
    private UUID addressedFrom;
    private LocalDateTime createdAt;
    private Boolean open;
}

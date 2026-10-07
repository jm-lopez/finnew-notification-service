package com.juanmatiaslopez.notification_service.Entity;

import com.juanmatiaslopez.notification_service.Enums.NotificationStatus;
import com.juanmatiaslopez.notification_service.Enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientEmail;

    private String recipientPhone;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    private String transactionReference;

    private final LocalDateTime createdAt = LocalDateTime.now();
}

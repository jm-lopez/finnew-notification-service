package com.juanmatiaslopez.notification_service.Repository;

import com.juanmatiaslopez.notification_service.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

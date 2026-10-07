package com.juanmatiaslopez.notification_service.Kafka.Service;

import com.juanmatiaslopez.notification_service.Kafka.DTO.BalanceUpdateEvent;
import com.juanmatiaslopez.notification_service.Kafka.DTO.UserRegistrationEvent;
import com.juanmatiaslopez.notification_service.Service.EmailServiceInt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumerListener {

    private final EmailServiceInt emailService;

    @KafkaListener(topics = "user-register-event", groupId = "notification-group")
    public void consumerUserRegisteredEvent(UserRegistrationEvent event){
        log.info("Received user registration");
        try {
            emailService.sendWelcomeEmail(event);
        } catch (Exception e) {
            log.error("error sending mail out");
        }
    }

    @KafkaListener(topics = "balance-update-notification-event", groupId = "notification-group")
    public void consumerBalanceNotificationEvent(BalanceUpdateEvent event){
        log.info("Received balance update event");
        try {
            emailService.sendTransactionAlert(event);
        } catch (Exception e) {
            log.error("error sending mail out");
        }
    }
}

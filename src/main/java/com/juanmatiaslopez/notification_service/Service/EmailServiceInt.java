package com.juanmatiaslopez.notification_service.Service;

import com.juanmatiaslopez.notification_service.Kafka.DTO.BalanceUpdateEvent;
import com.juanmatiaslopez.notification_service.Kafka.DTO.UserRegistrationEvent;

public interface EmailServiceInt {
    void sendWelcomeEmail (UserRegistrationEvent event);
    void sendTransactionAlert (BalanceUpdateEvent event);
}

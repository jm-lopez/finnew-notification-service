package com.juanmatiaslopez.notification_service.Kafka.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationEvent {

    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
    private String bankName = "Finnew";

}

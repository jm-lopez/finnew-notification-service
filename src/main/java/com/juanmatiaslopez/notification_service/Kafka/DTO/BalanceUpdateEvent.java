package com.juanmatiaslopez.notification_service.Kafka.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.juanmatiaslopez.notification_service.Enums.Transaction.Currency;
import com.juanmatiaslopez.notification_service.Enums.Transaction.TransactionDirection;
import com.juanmatiaslopez.notification_service.Enums.Transaction.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceUpdateEvent {

    //Transaction
    private String accountNumber;
    private BigDecimal amount;
    private TransactionDirection transactionDirection;
    private TransactionStatus transactionStatus;
    private String transactionReference;
    private Currency currency;

    //User
    private String email;
    private String firstName;
    private BigDecimal currentBalance;
    private String description;
}

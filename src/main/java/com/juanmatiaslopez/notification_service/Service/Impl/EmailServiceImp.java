package com.juanmatiaslopez.notification_service.Service.Impl;

import com.juanmatiaslopez.notification_service.Entity.Notification;
import com.juanmatiaslopez.notification_service.Enums.NotificationStatus;
import com.juanmatiaslopez.notification_service.Enums.NotificationType;
import com.juanmatiaslopez.notification_service.Enums.Transaction.TransactionDirection;
import com.juanmatiaslopez.notification_service.Kafka.DTO.BalanceUpdateEvent;
import com.juanmatiaslopez.notification_service.Kafka.DTO.UserRegistrationEvent;
import com.juanmatiaslopez.notification_service.Repository.NotificationRepository;
import com.juanmatiaslopez.notification_service.Service.EmailServiceInt;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailServiceInt {

    //TODO Fix emails

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final NotificationRepository notificationRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendWelcomeEmail(UserRegistrationEvent event) {

        try{

            Context context = new Context();
            context.setVariable("firstName", event.getFirstName());
            context.setVariable("lastName", event.getLastName());
            context.setVariable("email", event.getEmail());
            context.setVariable("accountNumber", event.getAccountNumber());
            context.setVariable("bankName", event.getBankName());

            String htmlTemplate = templateEngine.process("Welcome-Email", context);

            Notification notificationToSend = Notification.builder()
                    .recipientEmail(event.getEmail())
                    .message(htmlTemplate)
                    .notificationStatus(NotificationStatus.SENT)
                    .notificationType(NotificationType.EMAIL)
                    .subject("Welcome to the Bank")
                    .build();

            sendEmailOut(event.getEmail(), notificationToSend.getSubject(), htmlTemplate);

            notificationRepository.save(notificationToSend);

            log.info("Welcome email sent successfully");
        } catch (Exception e) {
            log.error("Failed to sent email");
            log.error(e.getMessage());
            Notification notificationToSend = Notification.builder()
                    .recipientEmail(event.getEmail())
                    .message("Failed to sent email")
                    .notificationStatus(NotificationStatus.FAILED)
                    .notificationType(NotificationType.EMAIL)
                    .subject("Welcome to the Bank")
                    .build();

            notificationRepository.save(notificationToSend);

            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void sendTransactionAlert(BalanceUpdateEvent event) {
        try{

            Context context = new Context();
            context.setVariable("name", event.getFirstName());
            context.setVariable("BankName", "Finnew");
            context.setVariable("amount", event.getAmount());
            context.setVariable("currency", event.getCurrency());
            context.setVariable("reference", event.getTransactionReference());
            context.setVariable("accountNumber", event.getAccountNumber());
            context.setVariable("description", event.getDescription());
            context.setVariable("date", LocalDateTime.now());
            context.setVariable("balance", event.getCurrentBalance());

            String templateName;
            String subject;

            if (event.getTransactionDirection().equals(TransactionDirection.CREDIT)){
                templateName = "credit-alert";
                subject = "Credit alert";
            } else {
                templateName = "debit-alert";
                subject = "Debit alert";
            }

            String htmlTemplate = templateEngine.process(templateName, context);

            Notification notificationToSend = Notification.builder()
                    .recipientEmail(event.getEmail())
                    .message(htmlTemplate)
                    .notificationStatus(NotificationStatus.SENT)
                    .notificationType(NotificationType.EMAIL)
                    .subject(subject)
                    .build();

            sendEmailOut(event.getEmail(), notificationToSend.getSubject(), htmlTemplate);

            notificationRepository.save(notificationToSend);

            log.info("Transaction email sent successfully");

        }catch (Exception e){
            log.error("Failed to sent transaction email");
            Notification notificationToSend = Notification.builder()
                    .recipientEmail(event.getEmail())
                    .message("Failed to sent email")
                    .notificationStatus(NotificationStatus.FAILED)
                    .notificationType(NotificationType.EMAIL)
                    .subject("Transaction Alert")
                    .build();

            notificationRepository.save(notificationToSend);

            throw new RuntimeException(e.getMessage());
        }
    }


    private void sendEmailOut(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent, true);
        javaMailSender.send(message);
    }
}

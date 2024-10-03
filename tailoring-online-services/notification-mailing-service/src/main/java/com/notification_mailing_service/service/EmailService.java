package com.notification_mailing_service.service;

import com.notification_mailing_service.config.VerificationCodeGenerator;
import com.notification_mailing_service.exception.EmailNotFoundException;
import com.notification_mailing_service.model.EmailVerification;
import com.notification_mailing_service.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final EmailVerificationRepository emailVerificationRepository;

    @Async
    public CompletableFuture<Boolean> sendVerificationEmail(String email) {
        String subject = "Email Verification Code";
        String code = verificationCodeGenerator.generateVerificationCode();
        String body = getVerificationEmailTemplate(code);

        try {
            logger.info("Envoi de l'e-mail de vérification à : {}", email);
            sendEmail(email, subject, body);
            emailVerificationRepository.save(EmailVerification.builder().email(email).verificationCode(code).build());
            logger.info("E-mail de vérification envoyé et sauvegardé pour : {}", email);
            return CompletableFuture.completedFuture(true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Erreur lors de l'envoi de l'e-mail de vérification à : {}", email, e);
            return CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public CompletableFuture<Boolean> sendOTPByEmail(String email) {
        String otp = verificationCodeGenerator.generateVerificationCode();
        String subject = "OTP Verification";
        String emailText = getOtpLoginEmailTemplate(otp);

        try {
            logger.info("Envoi de l'OTP par e-mail à : {}", email);
            sendEmail(email, subject, emailText);
            emailVerificationRepository.save(EmailVerification.builder().email(email).verificationCode(otp).build());
            logger.info("OTP envoyé et sauvegardé pour : {}", email);
            return CompletableFuture.completedFuture(true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Erreur lors de l'envoi de l'OTP à : {}", email, e);
            return CompletableFuture.completedFuture(false);
        }
    }

    public Boolean verifyCode(String email, String code) {
        var userVerification = emailVerificationRepository.findByEmail(email).orElseThrow(() -> {
            logger.error("Aucun utilisateur trouvé pour l'email : {}", email);
            return new EmailNotFoundException();
        });

        if (userVerification != null && userVerification.getVerificationCode().equals(code)) {
            emailVerificationRepository.delete(userVerification);
            logger.info("Code de vérification correct pour l'email : {}. Entrée supprimée.", email);
            return true;
        } else {
            logger.warn("Échec de la vérification du code pour l'email : {}", email);
            return false;
        }
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("Tailoring-online@outlook.com", "Tailoring Online");
        helper.setReplyTo("Tailoring-online@outlook.com", "Tailoring Online");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        message.setSentDate(date);

        javaMailSender.send(message);
        logger.info("E-mail envoyé à : {}", to);
    }

    @Async
    public void contactUs(String name, String email, String phone, String message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String body = String.format(
                "<div style=\"font-family:Arial,sans-serif;line-height:1.5;padding:10px;\">"
                        + "<h2>Contact Us Email</h2>"
                        + "<p>Name : <strong>%s</strong></p>"
                        + "<p>Email : <strong>%s</strong></p>"
                        + "<p>Phone : <strong>%s</strong></p>"
                        + "<p>Message : <strong>%s</strong></p>"
                        + "</div>", name, email, phone, message);

        helper.setFrom("Tailoring-online@outlook.com", "Tailoring Online");
        helper.setReplyTo("Tailoring-online@outlook.com", "Tailoring Online");
        helper.setTo("Tailoring-online@outlook.com");
        helper.setSubject(String.format("Client with name %s sent a message", name));
        helper.setText(body, true);
        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        Date sentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        mimeMessage.setSentDate(sentDate);
        javaMailSender.send(mimeMessage);

        logger.info("Message de contact envoyé par : {}", email);
    }

    private String getVerificationEmailTemplate(String verificationCode) {
        return String.format(
                "<div style=\"font-family:Arial,sans-serif;line-height:1.5;padding:10px;\">"
                        + "<h2>Email Verification</h2>"
                        + "<p>Your verification code is: <strong>%s</strong></p>"
                        + "<p>This code is valid for 10 minutes.</p>"
                        + "</div>", verificationCode);
    }

    private String getOtpLoginEmailTemplate(String otp) {
        return String.format(
                "<div style=\"font-family:Arial,sans-serif;line-height:1.5;padding:10px;\">"
                        + "<h2>OTP Verification</h2>"
                        + "<p>Your OTP code is: <strong>%s</strong></p>"
                        + "<p>This code is valid for 10 minutes.</p>"
                        + "</div>", otp);
    }
}

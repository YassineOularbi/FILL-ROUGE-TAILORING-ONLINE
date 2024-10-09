package com.notification_mailing_service.service;

import com.notification_mailing_service.config.VerificationCodeGenerator;
import com.notification_mailing_service.exception.EmailNotFoundException;
import com.notification_mailing_service.model.EmailVerification;
import com.notification_mailing_service.repository.EmailVerificationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final KafkaProducerService kafkaProducerService;
    private final KafkaConsumerService kafkaConsumerService;

    @Async
    @CircuitBreaker(name = "default", fallbackMethod = "fallbackSendVerificationEmail")
    public CompletableFuture<Boolean> sendVerificationEmail(String email) {
        kafkaProducerService.sendUserVerificationRequest(email);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while waiting for user verification response", e);
            return CompletableFuture.completedFuture(false);
        }

        if (!kafkaConsumerService.isUserExists()) {
            logger.warn("No user found with the email: {}", email);
            return CompletableFuture.completedFuture(false);
        }

        String code = verificationCodeGenerator.generateVerificationCode();
        String body = String.format(
                "<html dir=\"ltr\" xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">" +
                        "<style type=\"text/css\">" +
                        "body { font-family: Arial, sans-serif; background-color: #FAFAFA; margin: 0; padding: 0; }" +
                        ".container { width: 600px; margin: 0 auto; background-color: #FFFFFF; padding: 20px; border-radius: 5px; border: 1px solid black; margin: 10px; }" +
                        ".header { text-align: center; padding: 10px; }" +
                        ".header img { width: 140px; }" +
                        ".content { padding: 15px; }" +
                        ".footer { text-align: center; font-size: 12px; color: #CCCCCC; }" +
                        ".highlight { color: #5C68E2; font-weight: bold; }" +
                        ".greeting { font-size: 16px; }" +
                        ".message { margin: 15px 0; font-size: 14px; line-height: 1.5; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<div class=\"header\">" +
                        "<img src=\"https://i.ibb.co/mBfJvLc/logo.png\" alt=\"Logo\">" +
                        "<h1>Email Verification</h1>" +
                        "</div>" +
                        "<div class=\"content\">" +
                        "<p class=\"greeting\">Bonjour,</p>" +
                        "<p class=\"message\">Votre code de vérification est : <span class=\"highlight\">%s</span></p>" +
                        "<p class=\"message\">Ce code est valide pour 10 minutes.</p>" +
                        "<p class=\"message\">Si vous n'avez pas demandé cette vérification, veuillez ignorer cet e-mail.</p>" +
                        "</div>" +
                        "<div class=\"footer\">" +
                        "<p>Sent on: %s</p>" +
                        "<p>&copy; 2024 Tailoring Online. All Rights Reserved.</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                code,
                LocalDate.now()
        );
        try {
            logger.info("Sending verification email to: {}", email);
            sendEmail(email, "Email Verification Code", body);
            emailVerificationRepository.save(EmailVerification.builder()
                    .email(email)
                    .verificationCode(code)
                    .build());
            logger.info("Verification email sent and saved for: {}", email);
            return CompletableFuture.completedFuture(true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Error sending verification email to: {}", email, e);
            return CompletableFuture.completedFuture(false);
        }
    }

    public CompletableFuture<Boolean> fallbackSendVerificationEmail(String email, Throwable throwable) {
        logger.error("Fallback: Unable to send verification email to: {}", email, throwable);
        return CompletableFuture.completedFuture(false);
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

    public void contactUs(String name, String email, String phone, String message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String body = String.format(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                        "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                        "<html dir=\"ltr\" xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">" +
                        "<style type=\"text/css\">" +
                        "body { font-family: Arial, sans-serif; background-color: #FAFAFA; margin: 0; padding: 0; }"+
                        ".container { width: 600px; margin: 0 auto; background-color: #FFFFFF; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); border: 1px solid black; border-raduis: 10px; margin: 10px; }"+
                        ".header { text-align: center; padding: 10px; }"+
                        ".header img { width: 140px; }"+
                        ".content { padding: 15px; }"+
                        ".footer { text-align: center; font-size: 12px; color: #CCCCCC; }"+
                        ".highlight { color: #5C68E2; font-weight: bold; }"+
                        ".greeting { font-size: 16px; }"+
                        ".thank-you { font-size: 18px; font-weight: bold; color: #333333; }"+
                        ".message { margin: 15px 0; font-size: 14px; line-height: 1.5; }"+
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<div class=\"header\">" +
                        "<img src=\"https://i.ibb.co/mBfJvLc/logo.png\" alt=\"Logo\"> "+
                        "<h1>Contact Us</h1>" +
                        "</div>" +
                        "<div class=\"content\">" +
                        "<p class=\"greeting\">Bonjour <span class=\"highlight\">%s</span>,</p>" +
                        "<p class=\"thank-you\">Merci de nous avoir contactés !</p>" +
                        "<p class=\"message\">Nous avons bien reçu votre message et notre équipe est en train de le traiter. Voici les informations que vous avez fournies :</p>" +
                        "<p class=\"contact-info\"><strong>Name:</strong> %s<br>" +
                        "<strong>Email:</strong> %s<br>" +
                        "<strong>Phone:</strong> %s<br>" +
                        "<strong>Message:</strong></p>" +
                        "<p class=\"message\">%s</p>" +
                        "<p class=\"message\">Nous vous répondrons dans les plus brefs délais. En attendant, n’hésitez pas à explorer notre site pour découvrir nos dernières offres et services.</p>" +
                        "<p class=\"message\">Nous sommes là pour vous aider !</p>" +
                        "</div>" +
                        "<div class=\"footer\">" +
                        "<p>Sent on: %s</p>" +
                        "<p>&copy; 2024 Tailoring Online. All Rights Reserved.</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                name, name, email, phone, message, LocalDate.now());
        helper.setFrom("tailoring.online.1@gmail.com", "Tailoring Online");
        helper.setReplyTo("tailoring.online.1@gmail.com", "Tailoring Online");
        helper.setTo("tailoring.online.1@gmail.com");
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

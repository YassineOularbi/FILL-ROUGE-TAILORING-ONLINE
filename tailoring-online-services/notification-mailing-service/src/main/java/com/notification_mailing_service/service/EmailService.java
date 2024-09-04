package com.notification_mailing_service.service;

import com.notification_mailing_service.config.VerificationCodeGenerator;
import com.notification_mailing_service.exception.EmailNotFoundException;
import com.notification_mailing_service.model.EmailVerification;
import com.notification_mailing_service.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    private final JavaMailSender javaMailSender;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final EmailVerificationRepository emailVerificationRepository;

    @Async
    public CompletableFuture<Boolean> sendVerificationEmail(String email) {
        String subject = "Email Verification Code";
        String code = verificationCodeGenerator.generateVerificationCode();
        String body = getVerificationEmailTemplate(code);

        try {
            sendEmail(email, subject, body);
            emailVerificationRepository.save(EmailVerification.builder().email(email).verificationCode(code).build());
            return CompletableFuture.completedFuture(true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public CompletableFuture<Boolean> sendOTPByEmail(String email) {
        String otp = verificationCodeGenerator.generateVerificationCode();
        String subject = "OTP Verification";
        String emailText = getOtpLoginEmailTemplate(otp);

        try {
            sendEmail(email, subject, emailText);
            emailVerificationRepository.save(EmailVerification.builder().email(email).verificationCode(otp).build());
            return CompletableFuture.completedFuture(true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    public Boolean verifyCode(String email, String code) {
        var userVerification = emailVerificationRepository.findByEmail(email).orElseThrow(EmailNotFoundException::new);
        if (userVerification != null && userVerification.getVerificationCode().equals(code)) {
            emailVerificationRepository.delete(userVerification);
            return true;
        } else {
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

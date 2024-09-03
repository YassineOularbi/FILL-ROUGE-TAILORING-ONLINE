package com.user_management_service.service;

import com.user_management_service.config.VerificationCodeGenerator;
import com.user_management_service.model.EmailVerification;
import com.user_management_service.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationEmail(String to) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification Code";
        String code = verificationCodeGenerator.generateVerificationCode();
        String body = getVerificationEmailTemplate(code);
        sendEmail(to, subject, body);
        emailVerificationRepository.save(EmailVerification.builder().email(to).verificationCode(code).build());
    }

    public Boolean verifyCode(String email, String code) {
        var userVerification = emailVerificationRepository.findByEmail(email);
        return userVerification != null && userVerification.getVerificationCode().equals(code);
    }

    public void clearCode(String email) {
        var emailVerification = emailVerificationRepository.findByEmail(email);
        emailVerificationRepository.delete(emailVerification);
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
                        + "<h2>Email Verification</h2>"
                        + "<p>Your verification code is: <strong>%s</strong></p>"
                        + "<p>This code is valid for 10 minutes.</p>"
                        + "</div>", otp);
    }
}

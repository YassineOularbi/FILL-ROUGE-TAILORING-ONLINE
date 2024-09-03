package com.user_management_service.service;

import com.user_management_service.config.VerificationCodeGenerator;
import com.user_management_service.model.EmailVerification;
import com.user_management_service.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final VerificationCodeGenerator verificationCodeGenerator;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationEmail(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        var code = verificationCodeGenerator.generateVerificationCode();
        helper.setFrom("Tailoring-online@outlook.com", "Tailoring Online");
        helper.setReplyTo("Tailoring-online@outlook.com", "Tailoring Online");
        helper.setTo(to);
        helper.setSubject("Email Verification Code");
        helper.setText(STR."Your verification code is: \{code}");
        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        message.setSentDate(date);
        try {
            javaMailSender.send(message);
            emailVerificationRepository.save(EmailVerification.builder().email(to).verificationCode(code).build());
        } catch (Exception e){
            throw new RuntimeException("Error during saving code verification");
        }
    }

    public Boolean verifyCode(String email, String code) {
        var userVerification = emailVerificationRepository.findByEmail(email);
        return userVerification != null && userVerification.getVerificationCode().equals(code);
    }

    public void clearCode(String email){
        var emailVerification = emailVerificationRepository.findByEmail(email);
        emailVerificationRepository.delete(emailVerification);
    }
}

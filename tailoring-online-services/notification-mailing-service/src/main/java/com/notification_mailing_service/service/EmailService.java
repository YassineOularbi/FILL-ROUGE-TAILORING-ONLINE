package com.notification_mailing_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final OtpService otpService;

    public void sendEmailVerification(String email) throws IOException, MessagingException {
        String template = loadEmailTemplate("verification-mail");
        String processedTemplate = template.replace("${otpCode}", otpService.generateOtp(email));
        sendEmail(email, "Account verification request !", processedTemplate);
    }

    private String loadEmailTemplate(String name) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + name + ".html");
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
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
}
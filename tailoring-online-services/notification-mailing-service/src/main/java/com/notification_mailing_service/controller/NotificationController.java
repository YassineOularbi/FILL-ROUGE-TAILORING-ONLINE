package com.notification_mailing_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/notify-login")
    public void notifyLogin(@RequestParam String username) {
        messagingTemplate.convertAndSend("/topic/notifications", "Welcome back, " + username + "!");
    }

    @PostMapping("/notify-register")
    public void notifyRegister(@RequestParam String username) {
        messagingTemplate.convertAndSend("/topic/notifications", "Welcome " + username + "! Your registration is complete.");
    }
}


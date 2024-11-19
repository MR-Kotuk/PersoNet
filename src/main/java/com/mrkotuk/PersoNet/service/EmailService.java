package com.mrkotuk.PersoNet.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender sender;

    public void sendVerificatonEmail(String to, String token) {
        String subject = "Email Verification PersoNet";
        String verificationUrl = "http://localhost:8080/auth/verify-email?token=" + token;
        String text = "Please verify your email in PersoNet by clicking following link: " + verificationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);
    }
}
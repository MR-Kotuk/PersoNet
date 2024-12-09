package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.VerificationToken;
import com.mrkotuk.PersoNet.repo.VerificaionTokenRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageSenderService {
    private final JavaMailSender sender;
    private final VerificaionTokenRepo tokenRepo;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);
    }

    public String sendVerificationEmail(String email) {
        VerificationToken token = new VerificationToken(Integer.toString(new Random().nextInt(1000, 10000)), email);
        tokenRepo.save(token);

        sendEmail(email, "Email Verification PersoNet", "Your verification token is: " + token.getToken());

        return "Please verify email";
    }

    public String isVerified(String token) {
        VerificationToken verificationToken = tokenRepo.findByToken(token).get();
        tokenRepo.delete(verificationToken);

        return !verificationToken.getExpirationTime().isBefore(LocalDateTime.now())
                ? verificationToken.getEmail()
                : "";
    }
}
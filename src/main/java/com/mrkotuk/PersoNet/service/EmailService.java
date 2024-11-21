package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.model.VerificationToken;
import com.mrkotuk.PersoNet.repo.UserRepo;
import com.mrkotuk.PersoNet.repo.VerificaionTokenRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender sender;
    private final JWTService jwtService;
    private final UserRepo repo;
    private final VerificaionTokenRepo tokenRepo;

    public String sendVerificationEmail(User user) {
        VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user);
        repo.save(user);
        tokenRepo.save(token);
        sendVerificatonEmail(user.getEmail(), token.getToken());

        return "Please verify email. Open verification email, and click on the link.";
    }

    public String isVerified(String token) {
        VerificationToken verificationToken = tokenRepo.findByToken(token).get();

        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            tokenRepo.delete(verificationToken);
            return null;
        }

        User user = verificationToken.getUser();
        user.setVerified(true);

        tokenRepo.delete(verificationToken);
        repo.save(user);

        return jwtService.generateToken(verificationToken.getUser().getEmail());
    }

    private void sendVerificatonEmail(String to, String token) {
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
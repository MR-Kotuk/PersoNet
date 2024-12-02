package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.util.Random;

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
public class MessageSenderService {
    private final JavaMailSender sender;
    private final JWTService jwtService;
    private final UserRepo repo;
    private final VerificaionTokenRepo tokenRepo;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);
    }

    public String sendVerificationEmail(User user) {
        VerificationToken token = new VerificationToken(Integer.toString(new Random().nextInt(1000, 10000)), user);
        repo.save(user);
        tokenRepo.save(token);
        sendEmail(user.getEmail(), "Email Verification PersoNet", "Your verification token is: " + token.getToken());

        return "Please verify email";
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
}
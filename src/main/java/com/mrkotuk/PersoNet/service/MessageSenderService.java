package com.mrkotuk.PersoNet.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import com.mrkotuk.PersoNet.config.PathConfig;
import com.mrkotuk.PersoNet.exception.BadRequestException;
import com.mrkotuk.PersoNet.exception.InternalServerErrorException;
import org.jsoup.Jsoup;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.VerificationToken;
import com.mrkotuk.PersoNet.repository.VerificaionTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageSenderService {
    private final JavaMailSender sender;
    private final VerificaionTokenRepository tokenRepository;

    public String isVerified(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Trying to verify with a non-existent token: " + token));

        tokenRepository.delete(verificationToken);
        if (!verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return verificationToken.getEmail();
        } else {
            throw new BadRequestException("Token expired at: " + verificationToken.getExpirationTime());
        }
    }

    public void sendEmail(String sender, String to, String subject, String text) {
        String html = htmlToText(PathConfig.PERSON_MESSAGE);
        html = html.replace("[/subject/]", subject);
        html = html.replace("[/text/]", text);
        html = html.replace("[/sender/]", sender);

        sendEmail(to, subject, html);
    }

    public String sendVerificationEmail(String email) {
        VerificationToken token = new VerificationToken(
                Integer.toString(new Random().nextInt(1000, 10000)), email);
        tokenRepository.save(token);

        String html = htmlToText(PathConfig.VERIFICATION_MESSAGE);
        html = html.replace("[/token/]", token.getToken());

        sendEmail(email, "Email Verification Perso|||et", html);
        return "Please verify email";
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage mimeMessage = sender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            sender.send(mimeMessage);

            System.out.println("HTML email sent successfully.");
        } catch (MessagingException e) {
            throw new InternalServerErrorException("An error occurred while sending email to: " + to);
        }
    }

    private String htmlToText(String path) {
        try {
            return Jsoup.parse(new File(path), "UTF-8").html();
        } catch (IOException e) {
            throw new InternalServerErrorException("An error occurred while reading HTML file: " + path + "\n" + e);
        }
    }
}

package com.mrkotuk.PersoNet.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.VerificationToken;
import com.mrkotuk.PersoNet.repo.VerificaionTokenRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageSenderService {
    private final Logger logger = LoggerFactory.getLogger(MessageSenderService.class);

    private final String verificationMessagePath = "src/main/resources/static/verification.html";
    private final String messagePath = "src/main/resources/static/message.html";

    private final JavaMailSender sender;
    private final VerificaionTokenRepo tokenRepo;

    public String isVerified(String token) {
        VerificationToken verificationToken = tokenRepo.findByToken(token).get();
        tokenRepo.delete(verificationToken);

        return !verificationToken.getExpirationTime().isBefore(LocalDateTime.now())
                ? verificationToken.getEmail()
                : "";
    }

    public void sendEmail(String sender, String to, String subject, String text) {
        String html = htmlToText(messagePath);
        html = html.replace("[/subject/]", subject);
        html = html.replace("[/text/]", text);
        html = html.replace("[/sender/]", sender);

        sendEmail(to, subject, html);
    }

    public ResponseEntity<String> sendVerificationEmail(String email) {
        VerificationToken token = new VerificationToken(Integer.toString(new Random().nextInt(1000, 10000)), email);
        tokenRepo.save(token);

        String html = htmlToText(verificationMessagePath);
        html = html.replace("[/token/]", token.getToken());

        sendEmail(email, "Email Verification Perso|||et", html);
        return new ResponseEntity<>("Please verify email", HttpStatus.ACCEPTED);
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
            logger.error("An error occurred while sending the message", e);
        }
    }

    private String htmlToText(String path) {
        try {
            File input = new File(path);
            Document doc = Jsoup.parse(input, "UTF-8");
            return doc.html();
        } catch (IOException e) {
            logger.error("An error occurred while convert HTML to String", e);
        }

        return null;
    }
}
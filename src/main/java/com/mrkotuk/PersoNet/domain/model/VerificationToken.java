package com.mrkotuk.PersoNet.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;
    private String email;

    private LocalDateTime expirationTime;

    public VerificationToken(String token, String email) {
        this.token = token;
        this.email = email;
        expirationTime = LocalDateTime.now().plusMinutes(1);
    }
}

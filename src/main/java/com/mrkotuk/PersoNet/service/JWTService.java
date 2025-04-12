package com.mrkotuk.PersoNet.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.mrkotuk.PersoNet.exception.InternalServerErrorException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    private final String tokenKey;

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            tokenKey = Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("Error generating token key: " + e.getMessage());
        }
    }

    public String generateToken(String key) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder().claims().add(claims).subject(key)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and().signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(tokenKey));
    }

    public String extractToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractToken(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

package com.mrkotuk.PersoNet.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.model.VerificationToken;

@Repository
public interface VerificaionTokenRepo extends JpaRepository<VerificationToken, Integer> {
    public Optional<VerificationToken> findByToken(String token);
}
package com.mrkotuk.PersoNet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.domain.model.VerificationToken;

@Repository
public interface VerificaionTokenRepository extends JpaRepository<VerificationToken, Integer> {
    Optional<VerificationToken> findByToken(String token);
}

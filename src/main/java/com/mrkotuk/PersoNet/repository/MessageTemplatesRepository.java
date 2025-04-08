package com.mrkotuk.PersoNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.domain.model.MessageTemplate;

@Repository
public interface MessageTemplatesRepository extends JpaRepository<MessageTemplate, Integer> {
    List<MessageTemplate> findByEmail(String email);
}

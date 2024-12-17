package com.mrkotuk.PersoNet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.model.MessageTemplate;

@Repository
public interface MessageTemplatesRepo extends JpaRepository<MessageTemplate, Integer> {
    public List<MessageTemplate> findByEmail(String email);
}
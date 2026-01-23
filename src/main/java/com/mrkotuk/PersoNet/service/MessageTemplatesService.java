package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.MessageTemplate;
import com.mrkotuk.PersoNet.repository.MessageTemplatesRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageTemplatesService {
    private final MessageTemplatesRepository messageRepository;

    public List<MessageTemplate> getMessageTemplates(String email) {
        return messageRepository.findByEmail(email);
    }

    public MessageTemplate addMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return messageRepository.save(template);
    }

    public void deleteMessageTemplate(List<Integer> id) {
        messageRepository.deleteAllById(id);
    }
}

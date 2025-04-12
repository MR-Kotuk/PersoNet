package com.mrkotuk.PersoNet.service;

import java.util.List;

import com.mrkotuk.PersoNet.exception.NotFoundException;
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

    public MessageTemplate getMessageTemplateById(int id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found message template with id: " + id));
    }

    public MessageTemplate addMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return messageRepository.save(template);
    }

    public MessageTemplate updateMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return messageRepository.save(template);
    }

    public void deleteMessageTemplate(List<Integer> id) {
        messageRepository.deleteAllById(id);
    }
}

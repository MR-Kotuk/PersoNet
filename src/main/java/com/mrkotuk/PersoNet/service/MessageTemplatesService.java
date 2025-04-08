package com.mrkotuk.PersoNet.service;

import java.util.List;
import java.util.Optional;

import com.mrkotuk.PersoNet.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.MessageTemplate;
import com.mrkotuk.PersoNet.repository.MessageTemplatesRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageTemplatesService {
    private final MessageTemplatesRepository repository;

    public List<MessageTemplate> getMessageTemplates(String email) {
        List<MessageTemplate> templates = repository.findByEmail(email);
        if (templates == null || templates.isEmpty()) {
            throw new NotFoundException("Message templates not found");
        }

        return templates;
    }

    public MessageTemplate getMessageTemplateById(int id) {
        Optional<MessageTemplate> template = repository.findById(id);
        if (template.isEmpty()) {
            throw new NotFoundException("Message template not found");
        }

        return template.get();
    }

    public MessageTemplate addMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return repository.save(template);
    }

    public MessageTemplate updateMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return repository.save(template);
    }

    public String deleteMessageTemplate(List<Integer> id) {
        repository.deleteAllById(id);
        return "Message template deleted successfully";
    }
}

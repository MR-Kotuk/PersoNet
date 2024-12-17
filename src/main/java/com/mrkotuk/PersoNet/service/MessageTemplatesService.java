package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.MessageTemplate;
import com.mrkotuk.PersoNet.repo.MessageTemplatesRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageTemplatesService {
    private final MessageTemplatesRepo repo;

    public List<MessageTemplate> getMessageTemplates(String email) {
        return repo.findByEmail(email);
    }

    public MessageTemplate getMessageTemplateById(int id) {
        return repo.findById(id).get();
    }

    public MessageTemplate addMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return repo.save(template);
    }

    public MessageTemplate updateMessageTemplate(String email, MessageTemplate template) {
        template.setEmail(email);
        return repo.save(template);
    }

    public String deleteMessageTemplate(List<Integer> id) {
        repo.deleteAllById(id);
        return "Message template deleted successfully";
    }
}
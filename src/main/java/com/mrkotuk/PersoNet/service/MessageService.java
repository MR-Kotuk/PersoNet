package com.mrkotuk.PersoNet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.model.LineTemplate;
import com.mrkotuk.PersoNet.domain.dto.MessageDTO;
import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.repository.PersonMessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {
    private final PersonMessageRepository personMessageRepository;
    private final MessageSenderService senderService;
    private final UserService userService;

    private final String startSymbol = "[</";
    private final String endSymbol = "/>]";

    public List<Person> getPersonsWithEmail(String email) {
        return personMessageRepository.findByEmailAndStatus(email, PersonStatus.ACTIVE);
    }

    public List<String> getSharedLines(List<Integer> id) {
        List<String> sharedLines = personMessageRepository.findSharedLineTemplateNamesByPersonId(id, id.size());
        List<String> sharedLinesWithSymbols = new ArrayList<>();

        for (String line : sharedLines)
            sharedLinesWithSymbols.add(startSymbol + line + endSymbol);

        return sharedLinesWithSymbols;
    }

    public void sendMessage(String email, MessageDTO message) {
        String sender = userService.getUserByEmail(email).getUsername();

        for (Person person : personMessageRepository.findAllById(message.getRecipient())) {
            String to = "";

            for (LineTemplate line : person.getLineTemplates())
                if (line.getLineName().equals("Email"))
                    to = line.getLineValue();

            if (!to.isEmpty())
                senderService.sendEmail(sender, to,
                        getPersonaliseMessage(message.getSubject(), person),
                        getPersonaliseMessage(message.getMessage(), person));
        }
    }

    private String getPersonaliseMessage(String message, Person person) {
        for (LineTemplate line : person.getLineTemplates()) {
            String placeholder = startSymbol + line.getLineName() + endSymbol;
            if (message.contains(placeholder))
                message = message.replace(placeholder, line.getLineValue());
        }

        return message.replaceAll("\\[</([a-zA-Z0-9_]+/)>]", "Unknown");
    }
}

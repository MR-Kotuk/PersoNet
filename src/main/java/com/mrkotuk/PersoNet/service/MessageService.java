package com.mrkotuk.PersoNet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.model.LineTemplate;
import com.mrkotuk.PersoNet.model.Message;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.repo.PersonSenderRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageService {
    private final PersonSenderRepo repo;
    private final MessageSenderService service;
    private final UserService userService;

    private final String startSymbol = "[</";
    private final String endSymbol = "/>]";

    public List<Person> getPersonsWithEmail(String email) {
        return repo.findByEmailAndStatusAndValidLineTemplate(email, PersonStatus.ACTIVE);
    }

    public List<String> getSharedLines(List<Integer> id) {
        List<String> sharedLines = repo.findSharedLineTemplateNamesByPersonId(id, id.size());
        List<String> sharedLinesWithSymbols = new ArrayList<>();

        for (String line : sharedLines)
            sharedLinesWithSymbols.add(startSymbol + line + endSymbol);

        return sharedLinesWithSymbols;
    }

    public void sendMessage(String email, Message message) {
        String sender = userService.getUserByEmail(email).getUsername();

        for (Person person : repo.findAllById(message.getRecipient())) {
            String to = "";

            for (LineTemplate line : person.getLineTemplates())
                if (line.getLineName().equals("Email"))
                    to = line.getLineValue();

            if (!to.isEmpty())
                service.sendEmail(sender, to,
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

        message = message.replaceAll("\\[</([a-zA-Z0-9_]+/)>]", "Unknown");

        return message;
    }
}
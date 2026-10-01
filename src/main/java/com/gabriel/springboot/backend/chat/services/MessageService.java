package com.gabriel.springboot.backend.chat.services;

import com.gabriel.springboot.backend.chat.config.ChatProperties;
import com.gabriel.springboot.backend.chat.models.Message;
import com.gabriel.springboot.backend.chat.repositories.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final ChatProperties properties;

    public MessageService(MessageRepository repository, ChatProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    /** Latest messages in chronological order (oldest first). */
    public List<Message> findRecent() {
        var page = PageRequest.of(0, properties.historySize(), Sort.by(Sort.Direction.DESC, "date"));
        List<Message> latest = new ArrayList<>(repository.findAll(page).getContent());
        Collections.reverse(latest);
        return latest;
    }

    public Message save(Message message) {
        return repository.save(message);
    }
}

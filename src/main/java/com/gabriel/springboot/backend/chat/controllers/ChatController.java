package com.gabriel.springboot.backend.chat.controllers;

import com.gabriel.springboot.backend.chat.constants.ApiPaths;
import com.gabriel.springboot.backend.chat.dto.MessageRequest;
import com.gabriel.springboot.backend.chat.models.Message;
import com.gabriel.springboot.backend.chat.models.MessageType;
import com.gabriel.springboot.backend.chat.services.MessageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private static final List<String> COLORS =
            List.of("red", "green", "blue", "yellow", "orange", "magenta", "cyan", "purple");
    private static final Pattern CLIENT_ID = Pattern.compile("[\\w-]{1,64}");

    private final MessageService service;
    private final SimpMessagingTemplate websocket;

    public ChatController(MessageService service, SimpMessagingTemplate websocket) {
        this.service = service;
        this.websocket = websocket;
    }

    @MessageMapping(ApiPaths.MESSAGE)
    @SendTo(ApiPaths.TOPIC_MESSAGE)
    public Message receiveMessage(@Payload @Valid MessageRequest request) {
        long now = System.currentTimeMillis();

        if (request.type() == MessageType.NEW_USER) {
            return new Message(null, "new user", now, request.username(), MessageType.NEW_USER, randomColor());
        }

        if (request.text() == null || request.text().isBlank()) {
            return null;
        }
        return service.save(new Message(null, request.text(), now, request.username(),
                request.type(), request.color()));
    }

    @MessageMapping(ApiPaths.WRITING)
    @SendTo(ApiPaths.TOPIC_WRITING)
    public String isWriting(@Payload String username) {
        return username.concat(" has written...");
    }

    @MessageMapping(ApiPaths.HISTORY)
    public void sendHistory(@Payload String clientId) {
        if (clientId == null || !CLIENT_ID.matcher(clientId).matches()) {
            log.warn("History request ignored: invalid clientId");
            return;
        }
        websocket.convertAndSend(ApiPaths.TOPIC_HISTORY.concat(clientId), service.findRecent());
    }

    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    public void handleInvalidMessage(MethodArgumentNotValidException ex) {
        log.warn("Message discarded because it is invalid: {}", ex.getMessage());
    }

    private static String randomColor() {
        return COLORS.get(ThreadLocalRandom.current().nextInt(COLORS.size()));
    }
}

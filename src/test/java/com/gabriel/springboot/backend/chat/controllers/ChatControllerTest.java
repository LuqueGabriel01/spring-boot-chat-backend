package com.gabriel.springboot.backend.chat.controllers;

import com.gabriel.springboot.backend.chat.dto.MessageRequest;
import com.gabriel.springboot.backend.chat.models.Message;
import com.gabriel.springboot.backend.chat.models.MessageType;
import com.gabriel.springboot.backend.chat.services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    private MessageService service;
    private SimpMessagingTemplate websocket;
    private ChatController controller;

    @BeforeEach
    void setUp() {
        service = mock(MessageService.class);
        websocket = mock(SimpMessagingTemplate.class);
        controller = new ChatController(service, websocket);
    }

    @Test
    void newUserGetsColorAndIsNotSaved() {
        Message result = controller.receiveMessage(new MessageRequest(MessageType.NEW_USER, "gabriel", null, null));

        assertEquals("new user", result.text());
        assertEquals(MessageType.NEW_USER, result.type());
        assertNotNull(result.color());
        assertNotNull(result.date());
        verify(service, never()).save(any());
    }

    @Test
    void normalMessageIsSavedWithServerDate() {
        when(service.save(any(Message.class))).thenAnswer(inv -> inv.getArgument(0));

        Message result = controller.receiveMessage(new MessageRequest(MessageType.MESSAGE, "gabriel", "hola", "red"));

        assertEquals("hola", result.text());
        assertEquals("red", result.color());
        assertNotNull(result.date());
        verify(service).save(any(Message.class));
    }

    @Test
    void blankMessageIsIgnored() {
        assertNull(controller.receiveMessage(new MessageRequest(MessageType.MESSAGE, "gabriel", "   ", "red")));
        verify(service, never()).save(any());
    }

    @Test
    void historyIsSentToTheClientDestination() {
        List<Message> history = List.of(new Message("1", "hola", 1L, "gabriel", MessageType.MESSAGE, "red"));
        when(service.findRecent()).thenReturn(history);

        controller.sendHistory("client-123");

        verify(websocket).convertAndSend("/chat/history/client-123", history);
    }

    @Test
    void historyWithUnsafeClientIdIsIgnored() {
        controller.sendHistory("../admin/x");

        verifyNoInteractions(websocket);
    }
}

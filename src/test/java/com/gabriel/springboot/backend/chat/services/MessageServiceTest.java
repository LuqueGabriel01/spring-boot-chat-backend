package com.gabriel.springboot.backend.chat.services;

import com.gabriel.springboot.backend.chat.config.ChatProperties;
import com.gabriel.springboot.backend.chat.models.Message;
import com.gabriel.springboot.backend.chat.models.MessageType;
import com.gabriel.springboot.backend.chat.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageServiceTest {

    @Test
    void findRecentReturnsMessagesInChronologicalOrder() {
        MessageRepository repository = mock(MessageRepository.class);
        Message newest = new Message("2", "segundo", 200L, "ana", MessageType.MESSAGE, "red");
        Message oldest = new Message("1", "primero", 100L, "ana", MessageType.MESSAGE, "red");
        Page<Message> page = new PageImpl<>(List.of(newest, oldest));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        MessageService service = new MessageService(repository, new ChatProperties(List.of("*"), 10));

        assertEquals(List.of(oldest, newest), service.findRecent());
    }
}

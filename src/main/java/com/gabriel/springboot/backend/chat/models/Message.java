package com.gabriel.springboot.backend.chat.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Chat message. Immutable, stored in the {@code messages} collection.
 */
@Document(collection = "messages")
public record Message(
        @Id String id,
        String text,
        Long date,
        String username,
        MessageType type,
        String color) {
}

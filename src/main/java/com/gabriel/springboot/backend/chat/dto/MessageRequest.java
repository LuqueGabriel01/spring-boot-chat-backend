package com.gabriel.springboot.backend.chat.dto;

import com.gabriel.springboot.backend.chat.models.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * What the client sends. The server decides the id, the date and (for NEW_USER) the color,
 * so the client cannot tamper with them.
 */
public record MessageRequest(
        @NotNull MessageType type,
        @NotBlank @Size(max = 30) String username,
        @Size(max = 500) String text,
        @Size(max = 20) String color) {
}

package com.gabriel.springboot.backend.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * Chat settings, read from the properties with the {@code chat} prefix.
 *
 * @param allowedOrigins origins allowed to connect over WebSocket (CORS)
 * @param historySize    number of messages sent as history when a client connects
 */
@ConfigurationProperties(prefix = "chat")
public record ChatProperties(
        @DefaultValue("http://localhost:4200") List<String> allowedOrigins,
        @DefaultValue("10") int historySize) {
}

package com.gabriel.springboot.backend.chat.config;

import com.gabriel.springboot.backend.chat.constants.ApiPaths;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChatProperties properties;
    private final Validator validator;

    public WebSocketConfig(ChatProperties properties, Validator validator) {
        this.properties = properties;
        this.validator = validator;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(ApiPaths.WEBSOCKET_ENDPOINT)
                .setAllowedOrigins(properties.allowedOrigins().toArray(String[]::new))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(ApiPaths.BROKER_PREFIX);
        registry.setApplicationDestinationPrefixes(ApiPaths.APP_PREFIX);
    }

    /** Enables {@code @Valid} on {@code @MessageMapping} methods (STOMP does not validate by default). */
    public org.springframework.validation.Validator getValidator() {
        return new SpringValidatorAdapter(validator);
    }
}

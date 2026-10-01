package com.gabriel.springboot.backend.chat.constants;

public final class ApiPaths {

    public static final String WEBSOCKET_ENDPOINT = "/chat-websocket";

    public static final String APP_PREFIX = "/app";
    public static final String BROKER_PREFIX = "/chat";

    public static final String MESSAGE = "/message";
    public static final String WRITING = "/writing";
    public static final String HISTORY = "/history";

    public static final String TOPIC_MESSAGE = BROKER_PREFIX + MESSAGE;
    public static final String TOPIC_WRITING = BROKER_PREFIX + WRITING;
    public static final String TOPIC_HISTORY = BROKER_PREFIX + HISTORY + "/";

    private ApiPaths() {
    }
}

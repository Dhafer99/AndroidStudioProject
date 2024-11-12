package com.example.mobile.dto;

public class ChatMessage {
    private String userMessage;
    private String botResponse;

    public ChatMessage(String userMessage, String botResponse) {
        this.userMessage = userMessage;
        this.botResponse = botResponse;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getBotResponse() {
        return botResponse;
    }
}


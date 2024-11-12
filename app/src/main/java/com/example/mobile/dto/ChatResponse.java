package com.example.mobile.dto;

public class ChatResponse {
    private String user_message;
    private String bot_response;
    private String error;

    public String getUser_message() {
        return user_message;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }

    public String getBot_response() {
        return bot_response;
    }

    public void setBot_response(String bot_response) {
        this.bot_response = bot_response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}


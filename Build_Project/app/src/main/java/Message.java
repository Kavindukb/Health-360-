package com.example.bmi;

public class Message {
    private String text;
    private boolean isUser; // True if message is from user, false if from bot

    public Message(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    // Getters
    public String getText() {
        return text;
    }

    public boolean isUser() {
        return isUser;
    }
}

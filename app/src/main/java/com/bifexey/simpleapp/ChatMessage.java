package com.bifexey.simpleapp;

import java.util.Date;

public class ChatMessage {
    private String first_name;
    private String messageText;
    private String messagePhoneNumber;
    private long messageTime;

    public ChatMessage(String first_name, String messageText, String messageNumber) {
        this.first_name = first_name;
        this.messageText = messageText;
        this.messagePhoneNumber = messageNumber;

        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessagePhoneNumber() {
        return messagePhoneNumber;
    }

    public void setMessageEmail(String messageNumber) {
        this.messagePhoneNumber = messageNumber;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

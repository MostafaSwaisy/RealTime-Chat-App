package com.example.realtimechathomeworkfirebase.Model;

public class Chat {
    private String sender;
    private String receiver;
    private String Message;
    private Boolean isseen;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, Boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        Message = message;
        this.isseen = isseen;
    }

    public Boolean getIsseen() {
        return isseen;
    }

    public void setIsseen(Boolean isseen) {
        this.isseen = isseen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}

package com.example.chatinterface2;

import java.util.Date;

public class Message {
    public String userName;
    public String messageText;
    private long messageTime;

    public Message(){}
    public Message(String userName, String messageText){
        this.userName = userName;
        this.messageText = messageText;

        this.messageTime = new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

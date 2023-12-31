package com.example.wattsappchat.Models;

public class MessageModdel
{
    String uId, message, messageId;
    Long timestamp;



    public MessageModdel(String uId, String messageId, Long timestamp) {
        this.uId = uId;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public MessageModdel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessageModdel(){

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

package com.example.dreamweaver_refactor;

public class chat_componenets {
    public static String sent_from_user="me";
    public static String sent_from_gpt="bot";
    String message;
    String sent_by;

    public String getSent_from_user() {
        return sent_from_user;
    }

    public String getSent_from_gpt() {
        return sent_from_gpt;
    }

    public chat_componenets(String message, String sent_by) {
        this.message = message;
        this.sent_by = sent_by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }
}

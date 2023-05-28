package com.example.massenger;

public class message {
    private String text;
    private String from_who;
    private String to_who;
    private String when_sent;
    public message(String from_who, String to_who, String when_sent, String text){
        this.from_who =from_who;
        this.to_who= to_who;
        this.when_sent = when_sent;
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom_who() {
        return from_who;
    }

    public void setFrom_who(String from_who) {
        this.from_who = from_who;
    }

    public String getTo_who() {
        return to_who;
    }

    public void setTo_who(String to_who) {
        this.to_who = to_who;
    }

    public String getWhen_sent() {
        return when_sent;
    }

    public void setWhen_sent(String when_sent) {
        this.when_sent = when_sent;
    }
}

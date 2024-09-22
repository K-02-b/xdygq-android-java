package com.example.xdygq3;

public class EventMessage {

    private int type;
    private String message;

    public EventMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {

        return "type="+type+"--message= "+message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


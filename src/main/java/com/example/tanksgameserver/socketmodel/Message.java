package com.example.tanksgameserver.socketmodel;

public class Message {
    private String name;
    private String[] input;

    public Message() {}

    public Message(String name, String[] input) {
        this.name = name;
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getInput() {
        return input;
    }

    public void setInput(String[] input) {
        this.input = input;
    }
}

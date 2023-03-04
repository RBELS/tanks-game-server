package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;
import lombok.Setter;

public abstract class Message {
    @Getter @Setter
    private String name;

    public Message() {}

    public Message(String name) {
        this.name = name;
    }
}

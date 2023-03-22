package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;
import lombok.Setter;

public abstract class Message {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String lobbyId;

    public Message() {}

    public Message(String lobbyId, String name) {
        this.lobbyId = lobbyId;
        this.name = name;
    }
}

package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;
import lombok.Setter;

public abstract class Message {
    @Getter @Setter
    private String playerId;
    @Getter @Setter
    private String lobbyId;

    public Message() {}

    public Message(String lobbyId, String playerId) {
        this.lobbyId = lobbyId;
        this.playerId = playerId;
    }
}

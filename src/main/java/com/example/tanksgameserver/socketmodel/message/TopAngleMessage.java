package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;
import lombok.Setter;

public class TopAngleMessage extends Message {
    @Getter @Setter
    private double topAngle;

    public TopAngleMessage(String lobbyId, String name, double topAngle) {
        super(lobbyId, name);
        this.topAngle = topAngle;
    }
}

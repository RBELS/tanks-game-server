package com.example.tanksgameserver.socketmodel;

import lombok.Getter;
import lombok.Setter;

public class TopAngleMessage extends Message {
    @Getter @Setter
    private double topAngle;

    public TopAngleMessage(String name, double topAngle) {
        super(name);
        this.topAngle = topAngle;
    }
}

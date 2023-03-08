package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;

public class SimpleActionMessage extends Message {
    public static final int SHOOT_ACTION_ON     = 1;
    public static final int SHOOT_ACTION_OFF    = -1;

    @Getter
    private int action;

    public SimpleActionMessage(String name, int action) {
        super(name);
        this.action = action;
    }
}

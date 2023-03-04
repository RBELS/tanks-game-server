package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;

public class SimpleActionMessage extends Message {
    public static final int SHOOT_ACTION = 1;

    @Getter
    private int action;

    public SimpleActionMessage(String name, int action) {
        super(name);
        this.action = action;
    }
}

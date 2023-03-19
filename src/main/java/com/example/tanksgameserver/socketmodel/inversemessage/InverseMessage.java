package com.example.tanksgameserver.socketmodel.inversemessage;

import lombok.Getter;

public class InverseMessage {
    @Getter
    private final String type;

    public InverseMessage(String type) {
        this.type = type;
    }
}

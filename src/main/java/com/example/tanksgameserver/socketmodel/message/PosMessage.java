package com.example.tanksgameserver.socketmodel.message;

import lombok.Getter;
import lombok.Setter;

public class PosMessage extends Message {
    @Getter @Setter
    private String[] input;

    public PosMessage(String name, String[] input) {
        super(name);
        this.input = input;
    }
}

package com.example.tanksgameserver.socketmodel;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GameState {
    private Coordinates playerPos;

    public GameState(Coordinates playerPos) {
        this.playerPos = playerPos;
    }

    public GameState() {
        this.playerPos = new Coordinates();
    }

    public Coordinates getPlayerPos() {
        return playerPos;
    }

    public void setPlayerPos(Coordinates playerPos) {
        this.playerPos = playerPos;
    }
}

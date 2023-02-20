package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.GameState;

import java.util.HashMap;

public class UserGameState {
    private final double serverTime;
    private final HashMap<String, UserPlayerState> players;

    public UserGameState(GameState gs) {
        this.serverTime = gs.getPrevTime();
        this.players = UserPlayerState.createUserPlayerArr(gs.getPlayers());
    }

    public double getServerTime() {
        return serverTime;
    }

    public HashMap<String, UserPlayerState> getPlayers() {
        return players;
    }
}

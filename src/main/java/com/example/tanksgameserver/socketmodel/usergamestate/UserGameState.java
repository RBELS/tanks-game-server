package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.GameState;

import java.util.Map;

public class UserGameState {
    private final double serverTime;
    private final Map<String, UserPlayerState> players;

    public UserGameState(GameState gs) {
        this.serverTime = gs.getPrevTime();
        this.players = UserPlayerState.createUserPlayerArr(gs.getPlayers());
    }

    public double getServerTime() {
        return serverTime;
    }

    public Map<String, UserPlayerState> getPlayers() {
        return players;
    }
}

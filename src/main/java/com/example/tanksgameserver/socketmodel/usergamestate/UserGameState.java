package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.GameState;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class UserGameState {
    @Getter
    private final double serverTime;
    @Getter
    private final Map<String, UserPlayerState> players;
    @Getter
    private final List<UserBulletState> bullets;

    public UserGameState(GameState gs) {
        this.serverTime = gs.getPrevTime();
        this.players = UserPlayerState.createUserPlayerArr(gs.getPlayers());
        this.bullets = UserBulletState.createState(gs.getBullets());
    }
}

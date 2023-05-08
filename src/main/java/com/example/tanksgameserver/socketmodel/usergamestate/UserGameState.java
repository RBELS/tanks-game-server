package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.GameState;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class UserGameState {
    private final double serverTime;
    private final Map<String, UserPlayerState> players;
    private final List<UserBulletState> bullets;

    public UserGameState(GameState gs) {
        this.serverTime = gs.getPrevTime();
        this.players = UserPlayerState.createUserPlayerMap(gs.getPlayers());
        this.bullets = UserBulletState.createState(gs.getBullets());
    }
}

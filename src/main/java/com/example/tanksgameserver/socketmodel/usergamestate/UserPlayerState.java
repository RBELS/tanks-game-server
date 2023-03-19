package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.Player;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class UserPlayerState {
    @Getter
    private final double[] pos;
    @Getter
    private final int moveMultiplier;
    @Getter
    private final double bodyAngle;
    @Getter
    private final int bodyRotateMultiplier;
    @Getter
    private final double topRotateAngle;
    @Getter
    private final int topRotateMultiplier;
    @Getter
    private final int hp;
    @Getter
    private final int maxHp;

    public UserPlayerState(Player player) {
        this.pos = new double[] {player.getPos().getX(), player.getPos().getY()};
        this.moveMultiplier = player.getMoveMultiplier();
        this.bodyAngle = Math.toDegrees(player.getBodyAngle());
        this.bodyRotateMultiplier = player.getBodyRotateMultiplier();
        this.topRotateAngle = Math.toDegrees(player.getActualTopAngle());
        this.topRotateMultiplier = player.getTopRotateMultiplier();
        this.hp = player.getHp();
        this.maxHp = player.getMaxHP();
    }


    public static Map<String, UserPlayerState> createUserPlayerArr(Map<String, Player> players) {
        HashMap<String, UserPlayerState> userPlayers = new HashMap<>();
        for (String nickname : players.keySet()) {
            userPlayers.put(nickname, new UserPlayerState(players.get(nickname)));
        }
        return userPlayers;
    }
}

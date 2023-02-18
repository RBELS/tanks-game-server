package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.Player;

import java.util.HashMap;

public class UserPlayerState {
    private final double[] pos;
    private final int moveMultiplier;
    private final double bodyAngle;
    private final int bodyRotateMultiplier;

    public UserPlayerState(Player player) {
        this.pos = new double[] {player.getPos().getX(), player.getPos().getY()};
        this.moveMultiplier = player.getMoveMultiplier();
        this.bodyAngle = Math.toDegrees(player.getBodyAngle());
        this.bodyRotateMultiplier = player.getBodyRotateMultiplier();
    }

    public double[] getPos() {
        return pos;
    }

    public int getMoveMultiplier() {
        return moveMultiplier;
    }

    public double getBodyAngle() {
        return bodyAngle;
    }

    public int getBodyRotateMultiplier() {
        return bodyRotateMultiplier;
    }

    public static HashMap<String, UserPlayerState> createUserPlayerArr(HashMap<String, Player> players) {
        HashMap<String, UserPlayerState> userPlayers = new HashMap<>();
        for (String nickname : players.keySet()) {
            userPlayers.put(nickname, new UserPlayerState(players.get(nickname)));
        }
        return userPlayers;
    }
}

package com.example.tanksgameserver.socketmodel;

import com.example.tanksgameserver.socketmodel.usergamestate.UserGameState;
import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@Component
public class GameState {
    public static final Vector3D UP_VEC = new Vector3D(0.0, 1.0, 0.0);
    public static final Vector3D Z_AXIS_VEC = new Vector3D(0.0, 0.0, 1.0);
    public static final double PLAYER_SPEED = 6.0; //  UNITS/SEC
    public static final double PLAYER_ROTATE_SPEED = Math.toRadians(80.0); // DEG/SEC

    private final Logger logger = LoggerFactory.getLogger("Game State");

    private Double prevTime = null;
    private final HashMap<String, Player> players;

    public void addPlayer(String nickname) {
        players.put(nickname, new Player(nickname));
    }

    public Player removePlayer(String nickname) {
        return players.remove(nickname);
    }

    public void processPlayerMessage(Message message) {
        String username = message.getName();
        Player targetPlayer = players.get(username);
        if (targetPlayer == null) return;

        HashSet<String> newKeySet = new HashSet<>(Arrays.asList(message.getInput()));
        targetPlayer.setKeySet(newKeySet);
    }

    public GameState() {
        players = new HashMap<>();
    }

    public UserGameState createUserGameState() {
        return new UserGameState(this);
    }


    public void update() {
        if (prevTime == null) {
            this.prevTime = (double) System.currentTimeMillis();
        }

        double newTime = System.currentTimeMillis();

        double deltaTime = ((newTime - this.prevTime)) / 1000;
        this.prevTime = newTime;

        for (Player player : players.values()) {
            player.update(deltaTime);
        }
    }

    public Double getPrevTime() {
        return prevTime;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }
}

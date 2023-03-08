package com.example.tanksgameserver.socketmodel;

import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.usergamestate.UserGameState;
import lombok.Getter;
import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class GameState {
    public static final Vector3D UP_VEC = new Vector3D(0.0, 1.0, 0.0);
    public static final Vector3D Z_AXIS_VEC = new Vector3D(0.0, 0.0, 1.0);
    public static final double PLAYER_SPEED = 6.0; //  UNITS/SEC
    public static final double PLAYER_ROTATE_SPEED = Math.toRadians(80.0); // DEG/SEC
    public static final double PLAYER_TOP_ROTATE_SPEED = Math.toRadians(160.0); // DEG/SEC

    private final Logger logger = LoggerFactory.getLogger("Game State");

    @Getter
    private Double prevTime = null;
    @Getter
    private final Map<String, Player> players;
    @Getter
    private final List<Bullet> bullets;

    public void addPlayer(String nickname) {
        players.put(nickname, new Player(nickname));
    }

    public Player removePlayer(String nickname) {
        return players.remove(nickname);
    }

    public void processPlayerPosMessage(PosMessage posMessage) {
        String username = posMessage.getName();
        Player targetPlayer = players.get(username);
        if (targetPlayer == null) return;

        HashSet<String> newKeySet = new HashSet<>(Arrays.asList(posMessage.getInput()));
        targetPlayer.setKeySet(newKeySet);
    }

    public void processPlayerTopAngleMessage(TopAngleMessage topAngleMessage) {
        Player targetPlayer = players.get(topAngleMessage.getName());
        if (targetPlayer == null) return;

        targetPlayer.setDestTopAngle(Math.toRadians(topAngleMessage.getTopAngle()));
    }

    public GameState() {
        players = new ConcurrentHashMap<>();
        bullets = new CopyOnWriteArrayList<>();
    }

    public UserGameState createUserGameState() {
        return new UserGameState(this);
    }

    private void createBullet(String username) {
        Bullet newBullet = new Bullet(players.get(username));
        bullets.add(newBullet);
    }

    public void setShoot(String username, boolean on) {
        players.get(username).setShooting(on);
    }

    public void update() {
        if (prevTime == null) {
            this.prevTime = (double) System.currentTimeMillis();
        }

        double newTime = System.currentTimeMillis();

        double deltaTime = ((newTime - this.prevTime)) / 1000;
        this.prevTime = newTime;

        double finalNewTime = newTime / 1000;
        players.forEach((s, player) -> {
            player.update(deltaTime);
            if (player.isShooting() && finalNewTime - player.getLastShootTime() >= Player.RELOAD_TIME ) {
                player.setLastShootTime(finalNewTime);
                createBullet(player.getNickname());
            }
        });
        for (Bullet bullet : bullets) {
            boolean result = bullet.update(deltaTime);
            if (!result) {
                bullets.remove(bullet);
            }
        }
    }
}

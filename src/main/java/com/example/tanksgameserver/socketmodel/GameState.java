package com.example.tanksgameserver.socketmodel;

import com.example.tanksgameserver.socket.GameStateInverseWS;
import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.models.TankBodyModel;
import com.example.tanksgameserver.socketmodel.usergamestate.UserGameState;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import lombok.Getter;
import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameState extends Thread {
    private final GameStateInverseWS gameStateInverseWS;

    public static final Vector3D UP_VEC = new Vector3D(0.0, 1.0, 0.0);
    public static final Vector3D Z_AXIS_VEC = new Vector3D(0.0, 0.0, 1.0);
    public static final double PLAYER_SPEED = 6.0; //  UNITS/SEC
    public static final double PLAYER_ROTATE_SPEED = Math.toRadians(80.0); // DEG/SEC
    public static final double PLAYER_TOP_ROTATE_SPEED = Math.toRadians(160.0); // DEG/SEC
    public static final double HP_LOSS = 20.0; // HP/SEC
    public static final double GAME_MAP_WIDTH = 100.0;

    private final Logger logger = LoggerFactory.getLogger("Game State");
    private final TankBodyModel tankBodyModel;
    private final String lobbyId;

    @Getter
    private Double prevTime = null;
    @Getter
    private final Map<String, Player> players;
    @Getter
    private final List<Bullet> bullets;
    @Getter
    private final List<UserScore> userScores;

    public Player addPlayer(String nickname, String playerId) {
        Player newPlayer = new Player(nickname, playerId);
        players.put(playerId, newPlayer);
        updateScore(null, 0);
        gameStateInverseWS.sendScoreboardUpdateSignal(this.lobbyId);
        return newPlayer;
    }

    public Player removePlayer(String playerId) {
        return players.remove(playerId);
    }

    public void processPlayerPosMessage(PosMessage posMessage) {
        String playerId = posMessage.getPlayerId();
        Player targetPlayer = players.get(playerId);
        if (targetPlayer == null) return;

        HashSet<String> newKeySet = new HashSet<>(Arrays.asList(posMessage.getInput()));
        targetPlayer.setKeySet(newKeySet);
    }

    public void processPlayerTopAngleMessage(TopAngleMessage topAngleMessage) {
        Player targetPlayer = players.get(topAngleMessage.getPlayerId());
        if (targetPlayer == null) return;

        targetPlayer.setDestTopAngle(Math.toRadians(topAngleMessage.getTopAngle()));
    }

    public GameState(GameStateInverseWS gameStateInverseWS, String lobbyId) {
        players = new ConcurrentHashMap<>();
        bullets = new CopyOnWriteArrayList<>();
        tankBodyModel = new TankBodyModel();
        userScores = new ArrayList<>();
        this.lobbyId = lobbyId;

        this.gameStateInverseWS = gameStateInverseWS;
        this.start();
    }

    public UserGameState createUserGameState() {
        return new UserGameState(this);
    }

    private void createBullet(Player shootingPlayer) {
        Bullet newBullet = new Bullet(shootingPlayer);
        bullets.add(newBullet);
    }

    public void setShoot(String playerId, boolean on) {
        players.get(playerId).setShooting(on);
    }

    public void updateScore(Player player, int count) {
        if (player != null) {
            player.addScore(count);
        }
        UserScore.fillScoreList(this, this.userScores);
    }

    private void updateBullets(double deltaTime) {
        for (Bullet bullet : bullets) {
            boolean result = bullet.update(deltaTime);
            if (!result) {
                bullets.remove(bullet);
                break;
            }

            for (String nickname : players.keySet()) {
                Player curPlayer = players.get(nickname);
                if (Objects.isNull(curPlayer)) {
                    continue;
                }
                Vector3D pos = curPlayer.getPos();
                double rotateAngle = curPlayer.getBodyAngle();
                tankBodyModel.setModel(pos, rotateAngle);

                if (!bullet.getPlayer().equals(players.get(nickname)) && tankBodyModel.isInside(bullet.getPos())) {
                    logger.info("Penetration: " + nickname + "\t" + bullet.getPos().getX() + "\t" + bullet.getPos().getY());
                    boolean playerDead = curPlayer.hurt(Bullet.DEFAULT_DAMAGE);
                    if (playerDead) {
                        updateScore(bullet.getPlayer(), 1);
                        gameStateInverseWS.sendScoreboardUpdateSignal(this.lobbyId);
                        curPlayer.respawn();
                    }
                    bullets.remove(bullet);
                    break;
                }
            }
        }
    }

    private void updateGameMapBordersHP(double deltaTime) {
        AtomicBoolean shouldUpdateScoreboard = new AtomicBoolean(false);
        getPlayers().values().forEach(player -> {
            if (player.isOutField()) {
                boolean playerDead = player.hurt(deltaTime * GameState.HP_LOSS);
                if (playerDead) {
                    updateScore(player, -1);
                    shouldUpdateScoreboard.set(true);
                    player.respawn();
                }
            }
        });
        if (shouldUpdateScoreboard.get()) {
            gameStateInverseWS.sendScoreboardUpdateSignal(this.lobbyId);
        }
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
                createBullet(player);
            }
        });
        updateBullets(deltaTime);
        updateGameMapBordersHP(deltaTime);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            this.update();
        }
    }
}

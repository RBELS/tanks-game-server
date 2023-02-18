package com.example.tanksgameserver.socketmodel;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class GameState {
    private static final Vector3D UP_VEC = new Vector3D(0.0, 1.0, 0.0);
    private static final Vector3D Z_AXIS_VEC = new Vector3D(0.0, 0.0, 1.0);
    private static final double PLAYER_SPEED = 6.0; //  UNITS/SEC
    private static final double PLAYER_ROTATE_SPEED = Math.toRadians(80.0); // DEG/SEC

    private Vector3D playerPos;
    private Vector3D playerBodyDir;
    private double playerBodyAngle;

    private HashSet<String> keySet;

    private Long prevTime = null;

    public GameState(Vector3D playerPos, Vector3D playerBodyDir, double startAngle) {
        this.playerPos = playerPos;
        this.playerBodyDir = playerBodyDir;
        this.keySet = new HashSet<>();
        this.playerBodyAngle = startAngle;
    }

    public GameState() {
        this.playerPos = new Vector3D(0.0, 0.0, 0.0);
        this.playerBodyDir = new Vector3D(UP_VEC.getX(), UP_VEC.getY(), UP_VEC.getZ());
        this.keySet = new HashSet<>();
        this.playerBodyAngle = 0.0;
    }

    public Vector3D getPlayerPos() {
        return playerPos;
    }

    public void setPlayerPos(Vector3D playerPos) {
        this.playerPos = playerPos;
    }

    public void setKeySet(HashSet<String> keySet) {
        this.keySet = keySet;
    }

    public double getPlayerBodyAngle() {
        return playerBodyAngle;
    }

    public UserGameState createUserGameState() {
        return new UserGameState(this);
    }

    public void rotate(double angle) {
        if (angle == 0) return;

        playerBodyAngle += angle;
        Rotation rotation = new Rotation(Z_AXIS_VEC, playerBodyAngle);
        this.playerBodyDir = rotation.applyTo(UP_VEC);
    }

    public Vector3D getPlayerBodyDir() {
        return playerBodyDir;
    }

    public int getMoveMultiplier() {
        int multiplier = 0;
        if (keySet.contains("w")) {
            multiplier++;
        }
        if (keySet.contains("s")) {
            multiplier--;
        }
        return multiplier;
    }

    public int getBodyRotateMultiplier() {
        int multiplier = 0;
        if (keySet.contains("a")) {
            multiplier++;
        }
        if (keySet.contains("d")) {
            multiplier--;
        }
        return multiplier;
    }

    public void update() {
        if (prevTime == null) {
            this.prevTime = System.currentTimeMillis();
        }

        long newTime = System.currentTimeMillis();
        double deltaTime = ((double) (newTime - this.prevTime)) / 1000;
        this.prevTime = newTime;

        //calculate move distance
        //only 1 player now
        double distance = getMoveMultiplier() * deltaTime * PLAYER_SPEED;
        this.playerPos = this.playerPos.add(getPlayerBodyDir().scalarMultiply(distance));//will be replaced by player direction
        if (playerPos.getY() > 20.0) {
            playerPos = playerPos.negate();
        }

        //calculate rotate angle
        double rotAngle = getBodyRotateMultiplier() * deltaTime * PLAYER_ROTATE_SPEED;
        this.rotate(rotAngle);
    }
}

package com.example.tanksgameserver.socketmodel;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;

import java.util.HashSet;


public class Player {
    private final String nickname;
    private Vector3D pos;
    private Vector3D bodyDir;
    private double bodyAngle;
    private HashSet<String> keySet;

    public Player(String nickname) {
        this.nickname = nickname;
        this.pos = new Vector3D(0.0, 0.0, 0.0);
        this.bodyDir = new Vector3D(GameState.UP_VEC.getX(), GameState.UP_VEC.getY(), GameState.UP_VEC.getZ());
        this.bodyAngle = 0.0;
        this.keySet = new HashSet<>();
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

    public void rotate(double angle) {
        if (angle == 0) return;

        bodyAngle += angle;
        Rotation rotation = new Rotation(GameState.Z_AXIS_VEC, bodyAngle);
        bodyDir = rotation.applyTo(GameState.UP_VEC);
    }

    public void update(double deltaTime) {
        double distance = getMoveMultiplier() * deltaTime * GameState.PLAYER_SPEED;
        pos = pos.add(this.bodyDir.scalarMultiply(distance));

        //calculate rotate angle
        double rotAngle = getBodyRotateMultiplier() * deltaTime * GameState.PLAYER_ROTATE_SPEED;
        this.rotate(rotAngle);
    }




    public void setKeySet(HashSet<String> keySet) {
        this.keySet = keySet;
    }




    public String getNickname() {
        return nickname;
    }

    public Vector3D getPos() {
        return pos;
    }

    public Vector3D getBodyDir() {
        return bodyDir;
    }

    public double getBodyAngle() {
        return bodyAngle;
    }

    public HashSet<String> getKeySet() {
        return keySet;
    }
}

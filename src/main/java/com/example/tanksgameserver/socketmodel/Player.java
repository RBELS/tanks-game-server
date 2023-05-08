package com.example.tanksgameserver.socketmodel;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import java.util.HashSet;

public class Player {
    public static final double RELOAD_TIME = 1.0;
    public static final double START_HP = 100.0;
    @Getter
    private final String nickname;
    @Getter
    private final String playerId;
    @Getter
    private Vector3D pos;
    @Getter
    private Vector3D bodyDir;
    @Getter
    private double bodyAngle;
    @Getter @Setter
    private boolean shooting;
    @Getter @Setter
    private double lastShootTime;

    @Getter @Setter
    private double destTopAngle;
    @Getter
    private double actualTopAngle;
    @Getter @Setter
    private HashSet<String> keySet;

    @Getter
    private double hp;
    @Getter @Setter
    private double maxHP;
    @Getter @Setter
    private int score;
    public void addScore(int count) {
        this.score += count;
    }

    public Player(String nickname, String playerId) {
        this.nickname = nickname;
        this.playerId = playerId;
        this.pos = new Vector3D(0.0, 0.0, 0.0);
        this.bodyDir = new Vector3D(GameState.UP_VEC.getX(), GameState.UP_VEC.getY(), GameState.UP_VEC.getZ());
        this.bodyAngle = 0.0;
        this.keySet = new HashSet<>();
        this.destTopAngle = 0;
        this.actualTopAngle = 0;
        this.shooting = false;
        this.lastShootTime = 0.0;
        this.score = 0;
        this.hp = START_HP;
        this.maxHP = START_HP;
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

    private static final double TWO_PI = 2*Math.PI;
    public int getTopRotateMultiplier() {
//        logger.info("Dest: " + this.destTopAngle + "\tActual: " + this.actualTopAngle);
        if (Math.abs(Math.abs(this.destTopAngle) - Math.abs(this.actualTopAngle)) <= 0.005) return 0;


        if (this.actualTopAngle > TWO_PI)
            this.actualTopAngle -= TWO_PI;
        else if (this.actualTopAngle < 0)
            this.actualTopAngle += TWO_PI;


        if (Math.abs(this.destTopAngle - this.actualTopAngle) <= Math.PI) {
            if (this.destTopAngle >= this.actualTopAngle) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (this.destTopAngle >= this.actualTopAngle) {
                return -1;
            } else {
                return 1;
            }
        }


    }

    private void rotateBody(double angle) {
        if (angle == 0) return;

        bodyAngle += angle;
        Rotation rotation = new Rotation(GameState.Z_AXIS_VEC, bodyAngle);
        bodyDir = rotation.applyTo(GameState.UP_VEC);
    }

    public Vector3D getGunDir() {
        Rotation rotation = new Rotation(GameState.Z_AXIS_VEC, actualTopAngle);
        return rotation.applyTo(GameState.UP_VEC);
    }

    private void rotateTop(double angle) {
        if (angle == 0) return;

        actualTopAngle += angle;
    }

    public boolean hurt(double hurtHP) {
        this.hp = Math.max(0.0, this.hp-hurtHP);
        return this.hp == 0.0;
    }

    public void heal(double healHP) {
        this.hp = Math.min(this.maxHP, this.hp+healHP);
    }

    public void update(double deltaTime) {
        double distance = getMoveMultiplier() * deltaTime * GameState.PLAYER_SPEED;
        pos = pos.add(this.bodyDir.scalarMultiply(distance));

        //calculate rotate angle
        double bodyRotAngle = getBodyRotateMultiplier() * deltaTime * GameState.PLAYER_ROTATE_SPEED;
        this.rotateBody(bodyRotAngle);

        double topRotAngle = getTopRotateMultiplier() * deltaTime * GameState.PLAYER_TOP_ROTATE_SPEED;
        this.rotateTop(topRotAngle);
    }

    public void respawn() {
        heal(maxHP);
        pos = new Vector3D(Math.random() * GameState.GAME_MAP_WIDTH - GameState.GAME_MAP_WIDTH/2, Math.random() * GameState.GAME_MAP_WIDTH - GameState.GAME_MAP_WIDTH/2, 0.0);
    }

    public boolean isOutField() {
        return Math.abs(pos.getX()) > GameState.GAME_MAP_WIDTH/2
                || Math.abs(pos.getY()) > GameState.GAME_MAP_WIDTH/2;
    }

}

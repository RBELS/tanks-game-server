package com.example.tanksgameserver.socketmodel;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;


public class Player {
    public static Logger logger = LoggerFactory.getLogger("Player");
    @Getter
    private final String nickname;
    @Getter
    private Vector3D pos;
    @Getter
    private Vector3D bodyDir;
    @Getter
    private double bodyAngle;

    @Getter @Setter
    private double destTopAngle;

    @Getter
    private double actualTopAngle;

    @Getter @Setter
    private HashSet<String> keySet;

    public Player(String nickname) {
        this.nickname = nickname;
        this.pos = new Vector3D(0.0, 0.0, 0.0);
        this.bodyDir = new Vector3D(GameState.UP_VEC.getX(), GameState.UP_VEC.getY(), GameState.UP_VEC.getZ());
        this.bodyAngle = 0.0;
        this.keySet = new HashSet<>();
        this.destTopAngle = 0;
        this.actualTopAngle = 0;
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

    private void rotateTop(double angle) {
        if (angle == 0) return;

        actualTopAngle += angle;
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



}

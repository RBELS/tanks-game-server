package com.example.tanksgameserver.socketmodel;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math.geometry.Vector3D;

public class Bullet {
    public static final double BULLET_V = 20.0;
    public static final double BULLET_MAX_DISTANCE = 50.0;

    @Getter @Setter
    private Vector3D pos;
    @Getter @Setter
    private Vector3D flyDir;
    @Getter
    private final double flyAngle;
    @Getter
    private final double shootTime;
    @Getter
    private final Player player;

    public Bullet(Player player) {
        this.player = player;
        this.shootTime = System.currentTimeMillis()/1000.0;
        this.flyDir = player.getGunDir();
        this.flyAngle = player.getActualTopAngle();
        this.pos = new Vector3D(player.getPos().getX(), player.getPos().getY(), player.getPos().getZ());
    }

    public boolean update(double deltaTime) {
        double totalDistance = (System.currentTimeMillis()/1000.0 - shootTime) * BULLET_V;
        if (totalDistance > BULLET_MAX_DISTANCE)
            return false;

        pos = pos.add(flyDir.scalarMultiply(deltaTime*BULLET_V));
        return true;
    }
}

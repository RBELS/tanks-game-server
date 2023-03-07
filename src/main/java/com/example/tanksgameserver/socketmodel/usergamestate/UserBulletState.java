package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.Bullet;
import lombok.Getter;
import org.apache.commons.math.geometry.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class UserBulletState {
    @Getter
    private final double[] pos;
    @Getter
    private final double rotateAngle;

    public UserBulletState(Bullet bullet) {
        this.pos = new double[] {bullet.getPos().getX(), bullet.getPos().getY()};
        this.rotateAngle = Math.toDegrees(bullet.getFlyAngle());
    }

    public static List<UserBulletState> createState(List<Bullet> bullets) {
        return bullets.stream().map(UserBulletState::new).toList();
    }
}

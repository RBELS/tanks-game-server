package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.Bullet;
import lombok.Getter;

import java.util.List;

@Getter
public class UserBulletState {
    private final double[] pos;
    private final double rotateAngle;

    public UserBulletState(Bullet bullet) {
        this.pos = new double[] {bullet.getPos().getX(), bullet.getPos().getY()};
        this.rotateAngle = Math.toDegrees(bullet.getFlyAngle());
    }

    public static List<UserBulletState> createState(List<Bullet> bullets) {
        return bullets.stream().map(UserBulletState::new).toList();
    }
}

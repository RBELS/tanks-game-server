package com.example.tanksgameserver.socketmodel;

public class UserGameState {
    private double[] playerPos;
    private long serverTime;
    private double[] bodyMoveDir;

    public UserGameState(GameState gs) {
        this.playerPos = new double[] { gs.getPlayerPos().getX(), gs.getPlayerPos().getY() };
        this.serverTime = System.currentTimeMillis();
        this.bodyMoveDir = new double[] {gs.getPlayerBodyDir().getX(), gs.getPlayerBodyDir().getY()};
    }

    public double[] getPlayerPos() {
        return playerPos;
    }

    public long getServerTime() {
        return serverTime;
    }

    public double[] getBodyMoveDir() {
        return bodyMoveDir;
    }

}

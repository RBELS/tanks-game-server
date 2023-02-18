package com.example.tanksgameserver.socketmodel;

public class UserGameState {
    private double[] playerPos;
    private long serverTime;
    private int moveMultiplier;
    private double bodyAngle;
    private int bodyRotateMultiplier;

    public UserGameState(GameState gs) {
        this.playerPos = new double[] { gs.getPlayerPos().getX(), gs.getPlayerPos().getY() };
        this.serverTime = System.currentTimeMillis();
        this.moveMultiplier = gs.getMoveMultiplier();
        this.bodyAngle = Math.toDegrees(gs.getPlayerBodyAngle());
        this.bodyRotateMultiplier = gs.getBodyRotateMultiplier();
    }

    public double[] getPlayerPos() {
        return playerPos;
    }

    public long getServerTime() {
        return serverTime;
    }

    public int getMoveMultiplier() {
        return this.moveMultiplier;
    }

    public double getBodyAngle() {
        return bodyAngle;
    }

    public int getBodyRotateMultiplier() {
        return bodyRotateMultiplier;
    }
}

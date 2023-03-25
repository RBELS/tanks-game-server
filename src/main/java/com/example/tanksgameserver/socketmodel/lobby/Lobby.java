package com.example.tanksgameserver.socketmodel.lobby;

import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.Player;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import lombok.Getter;

import java.util.List;

public class Lobby {
    @Getter
    private final GameState gameState;
    @Getter
    private final String lobbyId;
    @Getter
    private final String lobbyName;
    @Getter
    private final Player lobbyLeader;

    public Lobby(GameState gameState, String lobbyId, String leaderUsername, String lobbyName) {
        this.gameState = gameState;
        this.lobbyId = lobbyId;
        this.lobbyName = lobbyName;
        this.lobbyLeader = gameState.addPlayer(leaderUsername);
    }

    public boolean playerExists(String nickname) {
        return gameState.getPlayers().containsKey(nickname);
    }

    public void createPlayer(String nickname) {
        gameState.addPlayer(nickname);
    }

    public void setShoot(String nickname, boolean on) {
        gameState.setShoot(nickname, on);
    }

    public List<UserScore> getScoreBoard() {
        return gameState.getUserScores();
    }

    public int getPlayersCount() {
        return gameState.getPlayers().size();
    }
}

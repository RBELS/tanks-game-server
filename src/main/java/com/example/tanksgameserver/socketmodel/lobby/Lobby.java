package com.example.tanksgameserver.socketmodel.lobby;

import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import lombok.Getter;

import java.util.List;

@Getter
public class Lobby {
    private final GameState gameState;
    private final String lobbyId;
    private final String lobbyName;
    private final String lobbyLeader;

    public Lobby(GameState gameState, String lobbyId, String leaderUsername, String lobbyName) {
        this.gameState = gameState;
        this.lobbyId = lobbyId;
        this.lobbyName = lobbyName;
        this.lobbyLeader = leaderUsername;
    }

    public boolean playerExists(String nickname) {
        return gameState.getPlayers().containsKey(nickname);
    }

    public void createPlayer(String nickname, String playerId) {
        gameState.addPlayer(nickname, playerId);
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

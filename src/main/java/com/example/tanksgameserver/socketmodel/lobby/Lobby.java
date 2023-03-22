package com.example.tanksgameserver.socketmodel.lobby;

import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import lombok.Getter;

import java.util.List;

public class Lobby {
    @Getter
    private final GameState gameState;
    @Getter
    private final String lobbyId;

    public Lobby(GameState gameState, String lobbyId) {
        this.gameState = gameState;
        this.lobbyId = lobbyId;
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
}

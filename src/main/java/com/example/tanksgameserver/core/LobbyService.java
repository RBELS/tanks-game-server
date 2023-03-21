package com.example.tanksgameserver.core;

import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.message.Message;
import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LobbyService extends Thread {
    private final GameState gameState;

    @Autowired
    public LobbyService(GameState gameState) {
        this.gameState = gameState;
        this.start();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void processPlayerMessage(Message message) {
        if (message instanceof PosMessage) {
            gameState.processPlayerPosMessage((PosMessage) message);
        } else if (message instanceof TopAngleMessage) {
            gameState.processPlayerTopAngleMessage((TopAngleMessage) message);
        }
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

    @Override
    public void run() {
        super.run();

        while (true) {
            this.gameState.update();
        }
    }
}

package com.example.tanksgameserver.core;

import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameService extends Thread {
    private final GameState gameState;

    @Autowired
    public GameService(GameState gameState) {
        this.gameState = gameState;
        this.start();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void processPlayerMessage(Message message) {
        gameState.processPlayerMessage(message);
    }

    public boolean playerExists(String nickname) {
        return gameState.getPlayers().containsKey(nickname);
    }

    public void createPlayer(String nickname) {
        gameState.addPlayer(nickname);
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            this.gameState.update();
        }
    }
}

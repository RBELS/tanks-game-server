package com.example.tanksgameserver.core;

import com.example.tanksgameserver.socketmodel.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

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

    public void setKeySet(HashSet<String> newKeySet) {
        gameState.setKeySet(newKeySet);
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            this.gameState.update();
        }
    }
}

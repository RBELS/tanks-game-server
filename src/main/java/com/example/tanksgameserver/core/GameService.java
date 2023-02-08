package com.example.tanksgameserver.core;

import com.example.tanksgameserver.socketmodel.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

}

package com.example.tanksgameserver.core;

import com.example.tanksgameserver.socket.GameStateInverseWS;
import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import com.example.tanksgameserver.socketmodel.message.Message;
import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class LobbyService extends Thread {
    //injected fields
    private final GameStateInverseWS gameStateInverseWS;
    //injected fields
    private final Map<String, Lobby> lobbiesMap;
    private static final String LOBBY_KEY = "lobby1";

    @Autowired
    public LobbyService(GameStateInverseWS gameStateInverseWS) {
        this.gameStateInverseWS = gameStateInverseWS;
        lobbiesMap = new HashMap<>();
        this.start();
    }

    public String createLobby() {
        Lobby newLobby = new Lobby(new GameState(gameStateInverseWS, LOBBY_KEY), LOBBY_KEY);
        lobbiesMap.put(LOBBY_KEY, newLobby);
        return LOBBY_KEY;
    }

    private String genLobbyId(int len) {
        int range = 'z' - 'a' + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i < len;i++) {
            int letterOffs = (int) Math.floor(Math.random() * range);
            stringBuilder.append((char) ('a' + letterOffs));
        }
        return stringBuilder.toString();
    }

    public GameState getGameState(String lobbyId) {
        return lobbiesMap.get(lobbyId).getGameState();
    }

    public void processPlayerMessage(Message message) {
        GameState gameState = lobbiesMap.get(message.getLobbyId()).getGameState();
        if (message instanceof PosMessage) {
            gameState.processPlayerPosMessage((PosMessage) message);
        } else if (message instanceof TopAngleMessage) {
            gameState.processPlayerTopAngleMessage((TopAngleMessage) message);
        }
    }

    public Lobby getLobby(String lobbyId) {
        return lobbiesMap.get(lobbyId);
    }

    public Map<String, Lobby> getLobbies() {
        return lobbiesMap;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            for (String lobbyId : lobbiesMap.keySet()) {
                GameState gameState = lobbiesMap.get(lobbyId).getGameState();
                gameState.update();
            }
        }
    }
}

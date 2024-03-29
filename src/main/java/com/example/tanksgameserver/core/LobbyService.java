package com.example.tanksgameserver.core;

import com.example.tanksgameserver.socket.GameStateInverseWS;
import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.Player;
import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import com.example.tanksgameserver.socketmodel.message.Message;
import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyService {
    //injected fields
    private final GameStateInverseWS gameStateInverseWS;
    //injected fields
    private final Map<String, Lobby> lobbiesMap;
    private static final int DEFAULT_LOBBY_ID_LEN = 6;

    public LobbyService(GameStateInverseWS gameStateInverseWS) {
        this.gameStateInverseWS = gameStateInverseWS;
        lobbiesMap = new ConcurrentHashMap<>();
    }

    public Lobby createLobby(String leaderUsername, String lobbyName) {
        String newLobbyId;
        do {
            newLobbyId = genLobbyId();
        } while (lobbiesMap.containsKey(newLobbyId));

        Lobby newLobby = new Lobby(new GameState(gameStateInverseWS, newLobbyId), newLobbyId, leaderUsername, lobbyName);
        lobbiesMap.put(newLobbyId, newLobby);
        return newLobby;
    }

    public void removeLobby(String lobbyId) {
        lobbiesMap.remove(lobbyId);
    }

    private String genLobbyId() {
        int range = 'z' - 'a' + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < LobbyService.DEFAULT_LOBBY_ID_LEN; i++) {
            int letterOffs = (int) Math.floor(Math.random() * range);
            stringBuilder.append((char) ('a' + letterOffs));
        }
        return stringBuilder.toString();
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

    public boolean usernameExists(String lobbyId, String username) {
        Map<String, Player> playerMap = lobbiesMap.get(lobbyId).getGameState().getPlayers();
        return playerMap.values().stream().anyMatch(player -> player.getNickname().equals(username));
    }

}

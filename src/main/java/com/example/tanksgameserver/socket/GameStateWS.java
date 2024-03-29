package com.example.tanksgameserver.socket;

import com.example.tanksgameserver.core.LobbyService;
import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.Player;
import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.SimpleActionMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.usergamestate.UserGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Controller
public class GameStateWS {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LobbyService lobbyService;
    private final GameStateInverseWS gameStateInverseWS;

    public GameStateWS(SimpMessagingTemplate simpMessagingTemplate, LobbyService lobbyService, GameStateInverseWS gameStateInverseWS) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.lobbyService = lobbyService;
        this.gameStateInverseWS = gameStateInverseWS;
    }

    private final Logger logger = LoggerFactory.getLogger("Websocket connection");

    @MessageMapping("/updatePos")
    public void updatePos(PosMessage posMessage) {
        lobbyService.processPlayerMessage(posMessage);
        logger.info(posMessage.getPlayerId() + "\t" + Arrays.toString(posMessage.getInput()));
    }

    @MessageMapping("/updateTopAngle")
    public void updateTopAngle(TopAngleMessage topAngleMessage) {
        lobbyService.processPlayerMessage(topAngleMessage);
    }

    @MessageMapping("/action")
    public void actionMessage(SimpleActionMessage message) {
        logger.info(message.getPlayerId() + "\t" + message.getAction());
        switch (message.getAction()) {
            case SimpleActionMessage.SHOOT_ACTION_ON -> lobbyService.getLobby(message.getLobbyId()).setShoot(message.getPlayerId(), true);
            case SimpleActionMessage.SHOOT_ACTION_OFF -> lobbyService.getLobby(message.getLobbyId()).setShoot(message.getPlayerId(), false);
        }
    }

    @Scheduled(fixedRate = 30)
    public void sendToEverybody() {
        Map<String, Lobby> lobbiesMap = lobbyService.getLobbies();
        Set<String> lobbyIds = lobbiesMap.keySet();
        lobbyIds.forEach(lobbyId -> {
            UserGameState state = lobbiesMap.get(lobbyId).getGameState().createUserGameState();
            simpMessagingTemplate.convertAndSend("/topic/gamestate/" + lobbyId, state);
        });
    }

    @EventListener
    public void listenEvent(SessionConnectEvent event) {
        var args = (Map<String, ArrayList<String>>) event.getMessage().getHeaders().get("nativeHeaders");

        String username = args.get("username").get(0);
        String lobbyId = args.get("lobbyId").get(0);
        String playerId = (String) event.getMessage().getHeaders().get("simpSessionId");

        if (username == null) return;
        logger.info("Login " + username + " " + playerId);

        if (lobbyService.usernameExists(lobbyId, username)) {
            return;
        }

        Lobby lobby = lobbyService.getLobby(lobbyId);
        if (!lobby.playerExists(username)) {
            lobby.createPlayer(username, playerId);
        }
    }

    @EventListener
    public void listenEvent(SessionDisconnectEvent event) {
        String playerId = (String) event.getMessage().getHeaders().get("simpSessionId");
        for (Lobby lobby : lobbyService.getLobbies().values()) {
            GameState gameState = lobby.getGameState();
            Player deletedPlayer = gameState.removePlayer(playerId);
            if (deletedPlayer == null) {
                continue;
            }

            gameState.updateScore(null, 0);
            deletedPlayer = null;
            if (lobby.getPlayersCount() == 0) {
                lobbyService.removeLobby(lobby.getLobbyId());
                break;
            }
            gameStateInverseWS.sendScoreboardUpdateSignal(lobby.getLobbyId());
            break;
        }
    }
}

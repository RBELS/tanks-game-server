package com.example.tanksgameserver.socket;

import com.example.tanksgameserver.core.LobbyService;
import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import com.example.tanksgameserver.socketmodel.message.PosMessage;
import com.example.tanksgameserver.socketmodel.message.SimpleActionMessage;
import com.example.tanksgameserver.socketmodel.message.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.usergamestate.UserGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Controller
public class GameStateWS {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private LobbyService lobbyService;

    private final Logger logger = LoggerFactory.getLogger("Websocket connection");

    @MessageMapping("/updatePos")
    public void updatePos(PosMessage posMessage) {
        lobbyService.processPlayerMessage(posMessage);
        logger.info(posMessage.getName() + "\t" + Arrays.toString(posMessage.getInput()));
    }

    @MessageMapping("/updateTopAngle")
    public void updateTopAngle(TopAngleMessage topAngleMessage) {
        lobbyService.processPlayerMessage(topAngleMessage);
    }

    @MessageMapping("/action")
    public void actionMessage(SimpleActionMessage message) {
        logger.info(message.getName() + "\t" + message.getAction());
        switch (message.getAction()) {
            case SimpleActionMessage.SHOOT_ACTION_ON -> lobbyService.getLobby(message.getLobbyId()).setShoot(message.getName(), true);
            case SimpleActionMessage.SHOOT_ACTION_OFF -> lobbyService.getLobby(message.getLobbyId()).setShoot(message.getName(), false);
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
}

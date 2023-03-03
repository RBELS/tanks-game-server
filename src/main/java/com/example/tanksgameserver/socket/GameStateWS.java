package com.example.tanksgameserver.socket;

import com.example.tanksgameserver.core.GameService;
import com.example.tanksgameserver.socketmodel.PosMessage;
import com.example.tanksgameserver.socketmodel.TopAngleMessage;
import com.example.tanksgameserver.socketmodel.usergamestate.UserGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Controller
public class GameStateWS {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private GameService gameService;

    private final Logger logger = LoggerFactory.getLogger("Websocket connection");

    @MessageMapping("/updatePos")
    public void updatePos(PosMessage posMessage) {
        gameService.processPlayerMessage(posMessage);
        logger.info(posMessage.getName() + "\t" + Arrays.toString(posMessage.getInput()));
    }

    @MessageMapping("/updateTopAngle")
    public void updateTopAngle(TopAngleMessage topAngleMessage) {
        gameService.processPlayerMessage(topAngleMessage);
    }

    @Scheduled(fixedRate = 30)//30
    public void sendToEverybody() {
        UserGameState state = gameService.getGameState().createUserGameState();
        simpMessagingTemplate.convertAndSend("/topic/gamestate", state);
    }
}

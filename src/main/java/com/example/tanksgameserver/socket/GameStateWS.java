package com.example.tanksgameserver.socket;

import com.example.tanksgameserver.core.GameService;
import com.example.tanksgameserver.socketmodel.Message;
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

    @MessageMapping("/update")
    public void updateState(Message message) {
        gameService.processPlayerMessage(message);
        logger.info(message.getName() + "\t" + Arrays.toString(message.getInput()));
    }

    @Scheduled(fixedRate = 30)//30
    public void sendToEverybody() throws InterruptedException {
        UserGameState state = gameService.getGameState().createUserGameState();
        simpMessagingTemplate.convertAndSend("/topic/gamestate", state);
    }
}

package com.example.tanksgameserver.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameStateInverseWS {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendUpdateSignal() {
        simpMessagingTemplate.convertAndSend("/topic/doUpdate", "scoreboard");
    }
}

package com.example.tanksgameserver.socket;

import com.example.tanksgameserver.socketmodel.inversemessage.InverseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameStateInverseWS {
    private enum ServerSignal {
        UPDATE_SCOREBOARD("UPDATE_SCOREBOARD"), UPDATE_HP("UPDATE_HP");

        ServerSignal(String signal) {
        }
    }

    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameStateInverseWS(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendScoreboardUpdateSignal(String lobbyId) {
        simpMessagingTemplate.convertAndSend("/topic/doUpdate/"+lobbyId, new InverseMessage(ServerSignal.UPDATE_SCOREBOARD.toString()));
    }
}

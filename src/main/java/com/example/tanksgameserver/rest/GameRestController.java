package com.example.tanksgameserver.rest;

import com.example.tanksgameserver.config.AppConfig;
import com.example.tanksgameserver.core.LobbyService;
import com.example.tanksgameserver.socketmodel.usergamestate.UserScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(value = AppConfig.SERVER_ADDRESS, allowCredentials = "true")
public class GameRestController {
    @Autowired
    private LobbyService lobbyService;

    private final Logger logger = LoggerFactory.getLogger("Game Rest Controller");

    @PostMapping (value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    private void createPlayer(@RequestBody Map<String, String> bodyParams) {
        String username = bodyParams.get("username");
        if (username == null) return;
        logger.info("Login " + username);

        if (!lobbyService.playerExists(username)) {
            lobbyService.createPlayer(username);
        }
    }

    @GetMapping (value = "/scoreboard", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<UserScore> getScoreboard() {
        return lobbyService.getScoreBoard();
    }
}

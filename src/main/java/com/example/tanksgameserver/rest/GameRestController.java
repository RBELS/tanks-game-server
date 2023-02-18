package com.example.tanksgameserver.rest;

import com.example.tanksgameserver.core.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(value = "http://192.168.1.36:3000", allowCredentials = "true")
public class GameRestController {
    @Autowired
    private GameService gameService;

    private final Logger logger = LoggerFactory.getLogger("Game Rest Controller");

    @PostMapping (value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    protected void createPlayer(@RequestBody Map<String, String> bodyParams) {
        String username = bodyParams.get("username");
        logger.info("Login " + username);

        if (!gameService.playerExists(username)) {
            gameService.createPlayer(username);
        }
    }
}

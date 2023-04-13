package com.example.tanksgameserver.rest;

import com.example.tanksgameserver.config.AppConfig;
import com.example.tanksgameserver.core.LobbyService;
import com.example.tanksgameserver.socketmodel.Player;
import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import com.example.tanksgameserver.socketmodel.usergamestate.UserLobby;
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

    @PostMapping (value = "/createLobby", produces = MediaType.APPLICATION_JSON_VALUE)
    private UserLobby createLobby(@RequestBody Map<String, String> bodyParams) {
        String username = bodyParams.get("username");
        String lobbyName = bodyParams.get("lobbyName");

        Lobby newLobby = lobbyService.createLobby(username, lobbyName);
        logger.info("Lobby " + newLobby.getLobbyId() + " created");
        return new UserLobby(newLobby);
    }

    @GetMapping (value = "/scoreboard/{lobbyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<UserScore> getScoreboard(@PathVariable("lobbyId") String lobbyId) {
        Lobby lobby = lobbyService.getLobby(lobbyId);
        return lobby.getScoreBoard();
    }

    @GetMapping (value = "/usernameExists/{lobbyId}/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    private boolean usernameExists(@PathVariable("lobbyId") String lobbyId, @PathVariable("username") String username) {
        return !lobbyService.usernameExists(lobbyId, username);
    }

    @GetMapping (value = "/lobbies")
    private List<UserLobby> getLobbies() {
        return lobbyService.getLobbies().
                values().stream().
                map(UserLobby::new).toList();
    }
}

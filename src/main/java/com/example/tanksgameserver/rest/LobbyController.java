package com.example.tanksgameserver.rest;

import com.example.tanksgameserver.core.LobbyService;
import com.example.tanksgameserver.responsemodel.ErrorResponse;
import com.example.tanksgameserver.validation.LobbyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class LobbyController {
    @Autowired
    private LobbyValidator lobbyValidator;
    @Autowired
    private LobbyService lobbyService;

    @PostMapping(value = "/lobby/new", produces = MediaType.APPLICATION_JSON_VALUE)
    protected ResponseEntity<Object> createLobby(@RequestParam() String username) {
        ErrorResponse errorResponse = lobbyValidator.validateUsername(username);
        if (errorResponse != null) {
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }

        lobbyService.createLobby(username);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping(value = "/lobbylist", produces = MediaType.APPLICATION_JSON_VALUE)
    protected Iterable<String> lobbyList() {
        return lobbyService.getLobbies().navigableKeySet();
    }
}

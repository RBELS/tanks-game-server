package com.example.tanksgameserver.socketmodel.usergamestate;

import lombok.Getter;

public class UserLobby {
    @Getter
    private final String lobbyId;

    public UserLobby(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}

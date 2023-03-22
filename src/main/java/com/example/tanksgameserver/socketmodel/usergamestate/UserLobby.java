package com.example.tanksgameserver.socketmodel.usergamestate;

import lombok.Getter;

public class UserLobby {
    @Getter
    private final String lobbyId;
//    @Getter
//    private final String leader;
//    @Getter
//    private final int playersCount;
//    @Getter
//    private final int maxPlayers;


    public UserLobby(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}

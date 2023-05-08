package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import lombok.Getter;

@Getter
public class UserLobby {
    private final String lobbyId;
    private final String leader;
    private final int playersCount;
    private final String lobbyName;

    public UserLobby(Lobby lobby) {
        this.lobbyId = lobby.getLobbyId();
        this.leader = lobby.getLobbyLeader();
        this.playersCount = lobby.getPlayersCount();
        this.lobbyName = lobby.getLobbyName();
    }
}

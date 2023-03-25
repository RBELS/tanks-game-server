package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.lobby.Lobby;
import lombok.Getter;

public class UserLobby {
    @Getter
    private final String lobbyId;
    @Getter
    private final String leader;
    @Getter
    private final int playersCount;
    @Getter
    private final String lobbyName;

    public UserLobby(Lobby lobby) {
        this.lobbyId = lobby.getLobbyId();
        this.leader = lobby.getLobbyLeader().getNickname();
        this.playersCount = lobby.getPlayersCount();
        this.lobbyName = lobby.getLobbyName();
    }
}

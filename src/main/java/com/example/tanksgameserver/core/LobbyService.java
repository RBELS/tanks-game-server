package com.example.tanksgameserver.core;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class LobbyService {
    private TreeMap<String, String> lobbies = new TreeMap<>(Comparator.naturalOrder());
    private int counter = 0;

    public void createLobby(String leaderUser) {
        lobbies.put(leaderUser, "Lobby" + (++counter));
    }

    public TreeMap<String, String> getLobbies() {
        return lobbies;
    }
}

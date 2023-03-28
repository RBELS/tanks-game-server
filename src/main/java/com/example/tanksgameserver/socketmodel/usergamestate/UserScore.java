package com.example.tanksgameserver.socketmodel.usergamestate;

import com.example.tanksgameserver.socketmodel.GameState;
import com.example.tanksgameserver.socketmodel.Player;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public record UserScore(@Getter String name, @Getter int score) {
    public static void fillScoreList(GameState gameState, List<UserScore> scoreArr) {
        scoreArr.clear();
        Map<String, Player> playerMap = gameState.getPlayers();
        for (Player somePlayer : playerMap.values()) {
            scoreArr.add(new UserScore(somePlayer.getNickname(), somePlayer.getScore()));
        }
        scoreArr.sort((o1, o2) -> o2.score - o1.score);
    }
}

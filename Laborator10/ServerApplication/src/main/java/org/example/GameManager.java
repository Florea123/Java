package org.example;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private Map<String, HexGame> games = new HashMap<>();

    public HexGame createGame(String gameId) {
        if (games.containsKey(gameId)) {
            throw new IllegalArgumentException("Game ID already exists");
        }
        HexGame game = new HexGame(gameId);
        games.put(gameId, game);
        return game;
    }

    public HexGame joinGame(String gameId, Player player) {
        HexGame game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }
        game.addPlayer(player);
        return game;
    }

    public HexGame getGame(String gameId) {
        return games.get(gameId);
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }

    public Map<String, HexGame> getGames() {
        return games;
    }
}
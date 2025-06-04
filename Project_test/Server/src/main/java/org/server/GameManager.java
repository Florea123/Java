package org.server;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.net.Socket;

public class GameManager {
    private final List<ChessGame> games = new ArrayList<>();
    private final Map<String, Socket> playerSockets = new HashMap<>();

    public synchronized ChessGame findOrCreateGame(String player) {
        for (ChessGame game : games) {
            if (!game.isFull()) {
                game.addPlayer(player);
                return game;
            }
        }
        ChessGame newGame = new ChessGame(player);
        games.add(newGame);
        return newGame;
    }

    public synchronized void removeGame(ChessGame game) {
        games.remove(game);
    }

    public synchronized ChessGame findGameByPlayer(String player) {
        for (ChessGame game : games) {
            if (game.getPlayers().contains(player)) {
                return game;
            }
        }
        return null;
    }

    public synchronized void addPlayerSocket(String player, Socket socket) {
        playerSockets.put(player, socket);
    }

    public synchronized Socket getSocketForPlayer(String player) {
        return playerSockets.get(player);
    }

    public synchronized void removePlayerSocket(String player) {
        playerSockets.remove(player);
    }
}

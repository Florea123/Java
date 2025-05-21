package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private GameServer server;
    private String currentGameId;

    public ClientThread(Socket clientSocket, GameServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.currentGameId = null;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received request: " + request);
                String[] parts = request.split(" ");
                String command = parts[0];

                switch (command.toLowerCase()) {
                    case "create":
                        if (currentGameId != null) {
                            out.println("Error: You are already in an active game. You cannot create a new game.");
                            break;
                        }
                        if (parts.length < 2) {
                            out.println("Error: Invalid create command. Usage: create <gameId>");
                        } else {
                            String gameId = parts[1];
                            try {
                                HexGame game = server.getGameManager().createGame(gameId);
                                Player creator = new Player("blue", 300,out);
                                game.addPlayer(creator);
                                currentGameId = gameId;
                                out.println("Game created with ID: " + gameId);
                            } catch (IllegalArgumentException e) {
                                out.println("Error: " + e.getMessage());
                            }
                        }
                        break;

                    case "join":
                        if (currentGameId != null) {
                            out.println("Error: You are already in an active game. You cannot join another game.");
                            break;
                        }
                        if (parts.length < 2) {
                            out.println("Error: Invalid join command. Usage: join <gameId>");
                        } else {
                            String gameId = parts[1];
                            try {
                                HexGame game = server.getGameManager().getGame(gameId);
                                if (game == null) {
                                    out.println("Error: Game with ID " + gameId + " does not exist.");
                                    break;
                                }
                                if (game.isGameStarted()) {
                                    out.println("Error: Cannot join. The game has already started.");
                                    break;
                                }
                                Player player = new Player("red", 300,out);
                                server.getGameManager().joinGame(gameId, player);
                                currentGameId= gameId;
                                out.println("A player joined the game with ID: " + gameId);
                            } catch (IllegalArgumentException e) {
                                out.println("Error: " + e.getMessage());
                            }
                        }
                        break;


                    case "move":
                        if (currentGameId == null) {
                            out.println("Error: You are not part of any game. Join or create a game first.");
                            break;
                        }
                        if (parts.length < 3) {
                            out.println("Error: Invalid move command. Usage: move <x> <y>");
                        } else {
                            String gameId = currentGameId;
                            int x, y;
                            try {
                                x = Integer.parseInt(parts[1]);
                                y = Integer.parseInt(parts[2]);
                            } catch (NumberFormatException e) {
                                out.println("Error: Coordinates must be integers.");
                                break;
                            }
                            try {
                                HexGame game = server.getGameManager().getGame(gameId);
                                if (game == null) {
                                    out.println("Error: Game with ID " + gameId + " does not exist.");
                                    break;
                                }
                                    Player currentPlayer = game.getCurrentPlayer();
                                if (currentPlayer == null) {
                                    out.println("Error: No current player in the game.");
                                    break;
                                }

                                if (x < 0 || x >= game.getBoardSize() || y < 0 || y >= game.getBoardSize()) {
                                    out.println("Error: Coordinates are out of bounds.");
                                    break;
                                }

                                if (game.getBoard()[x][y] != "\u26AA") {
                                    out.println("Error: Cell at (" + x + ", " + y + ") is already occupied.");
                                    break;
                                }

                                if (game.submitMove(currentPlayer, x, y)) {
                                    out.println("Move accepted. Player " + currentPlayer.getColor() + " wins!");
                                } else {
                                    out.println("Move accepted. Next turn: " + game.getCurrentPlayer().getColor());
                                }
                            } catch (IllegalStateException | IllegalArgumentException e) {
                                out.println("Error: " + e.getMessage());
                            }
                        }
                        break;

                    case "stop":
                        server.stop();
                        out.println("Server stopped");
                        break;

                    case "list":
                        if (currentGameId != null) {
                            out.println("Error: You are already in an active game.");
                            break;
                        }
                        try {
                            Map<String, HexGame> games = server.getGameManager().getGames();
                            StringBuilder availableGames = new StringBuilder("Available games:\n");
                            boolean found = false;

                            for (Map.Entry<String, HexGame> entry : games.entrySet()) {
                                HexGame game = entry.getValue();
                                if (!game.isGameStarted()) {
                                    availableGames.append("- ").append(game.getGameId()).append("\n");
                                    found = true;
                                }
                            }

                            if (!found) {
                                out.println("No available games at the moment.");
                            } else {
                                out.println(availableGames.toString());
                            }
                        } catch (Exception e) {
                            out.println("Error retrieving games: " + e.getMessage());
                        }
                        break;

                    default:
                        out.println("Unknown command: " + command);
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close client socket: " + e.getMessage());
            }
        }
    }
}
package org.server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final DatabaseManager dbManager = new DatabaseManager();
    private static final GameManager gameManager = new GameManager();
    private String username;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                String[] parts = message.split(" ");
                String command = parts[0];

                if ("LOGIN".equalsIgnoreCase(command)) {
                    String username = parts[1];
                    String password = parts[2];
                    if (!dbManager.doesUserExist(username)) {
                        out.println("ERROR User does not exist");
                    } else if (dbManager.isUserLoggedIn(username)) {
                        out.println("ERROR User already logged in");
                    } else if (dbManager.loginUser(username, password)) {
                        out.println("LOGIN_SUCCESS");
                    } else {
                        out.println("ERROR Invalid username or password");
                    }
                } else if ("REGISTER".equalsIgnoreCase(command)) {
                    String username = parts[1];
                    String password = parts[2];
                    if (dbManager.isUsernameTaken(username)) {
                        out.println("ERROR Username already exists");
                    } else if (dbManager.registerUser(username, password)) {
                        out.println("REGISTER_SUCCESS");
                    } else {
                        out.println("ERROR Registration failed");
                    }
                } else if ("LOGOUT".equalsIgnoreCase(command)) {
                    String username = parts[1];
                    dbManager.logoutUser(username);
                    out.println("LOGOUT_SUCCESS");
                } else if ("EXIT".equalsIgnoreCase(command)) {
                    String username = parts[1];
                    dbManager.logoutUser(username);
                    out.println("EXIT_SUCCESS");
                    break;
                } else if ("FIND_GAME".equalsIgnoreCase(command)) {
                    this.username = parts[1];

                    ChessGame game = gameManager.findOrCreateGame(username);
                    gameManager.addPlayerSocket(username, clientSocket);


                    if (game.isGameReady()) {
                        String whitePlayer = game.getWhitePlayer();
                        String blackPlayer = game.getBlackPlayer();


                        Socket whiteSocket = gameManager.getSocketForPlayer(whitePlayer);
                        if (whiteSocket != null) {
                            try {
                                PrintWriter whiteOut = new PrintWriter(whiteSocket.getOutputStream(), true);
                                whiteOut.println("GAME_READY white");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Socket blackSocket = gameManager.getSocketForPlayer(blackPlayer);
                        if (blackSocket != null) {
                            try {
                                PrintWriter blackOut = new PrintWriter(blackSocket.getOutputStream(), true);
                                blackOut.println("GAME_READY black");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        out.println("WAITING_FOR_PLAYER");
                    }
                } else if ("MOVE".equalsIgnoreCase(command)) {
                    int fromRow = Integer.parseInt(parts[1]);
                    int fromCol = Integer.parseInt(parts[2]);
                    int toRow = Integer.parseInt(parts[3]);
                    int toCol = Integer.parseInt(parts[4]);

                    ChessGame game = gameManager.findGameByPlayer(username);
                    if (game != null) {
                        if (!game.canPlayerMove(username)) {
                            out.println("ERROR It's not your turn");
                        } else if (game.movePiece(username, fromRow, fromCol, toRow, toCol)) {
                            game.switchTurn();
                            String boardState = serializeBoard(game.getBoard());

                            String kingInCheck = game.getKingInCheck();
                            if (kingInCheck != null) {
                                if (game.isCheckmate(kingInCheck)) {
                                    String winner = kingInCheck.equals("white") ? "black" : "white";
                                    notifyPlayers(game, "CHECKMATE " + winner);

                                    gameManager.removeGame(game);
                                    for (String player : game.getPlayers()) {
                                        gameManager.removePlayerSocket(player);
                                    }
                                } else {
                                    notifyPlayers(game, "CHECK " + kingInCheck);
                                }
                            }

                            notifyPlayers(game, "UPDATE_BOARD " + boardState);
                            out.println("MOVE_SUCCESS");
                        } else {
                            out.println("MOVE_INVALID");
                        }
                    } else {
                        out.println("ERROR Game not found");
                    }
                } else if ("GET_BOARD".equalsIgnoreCase(command)) {
                    ChessGame game = gameManager.findGameByPlayer(username);
                    if (game != null) {
                        StringBuilder boardState = new StringBuilder();
                        String[][] board = game.getBoard();
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                boardState.append(board[i][j] == null ? "" : board[i][j]);
                                if (j < 7) {
                                    boardState.append(",");
                                }
                            }
                            if (i < 7) {
                                boardState.append(";");
                            }
                        }
                        out.println(boardState.toString());
                    } else {
                        out.println("ERROR No game found for player");
                    }
                } else if ("GET_TURN".equalsIgnoreCase(command)) {
                    ChessGame game = gameManager.findGameByPlayer(username);
                    if (game != null) {
                        out.println(game.getCurrentTurn());
                    } else {
                        out.println("ERROR No game found for player");
                    }
                } else if ("EXIT_LOBBY".equalsIgnoreCase(command)) {
                    String username = parts[1];
                    ChessGame game = gameManager.findGameByPlayer(username);
                    if (game != null) {
                        gameManager.removeGame(game);
                    }
                    out.println("EXIT_LOBBY_SUCCESS");
                }
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void notifyPlayers(ChessGame game, String message) {
        try {
            for (String player : game.getPlayers()) {
                Socket playerSocket = gameManager.getSocketForPlayer(player);
                if (playerSocket != null) {
                    PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
                    playerOut.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String serializeBoard(String[][] board) {
        StringBuilder boardState = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardState.append(board[i][j] == null ? "" : board[i][j]);
                if (j < 7) {
                    boardState.append(",");
                }
            }
            if (i < 7) {
                boardState.append(";");
            }
        }
        return boardState.toString();
    }
}
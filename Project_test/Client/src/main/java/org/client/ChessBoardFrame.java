package org.client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChessBoardFrame extends JFrame {
    private final ChessClient client;
    private final String playerColor;
    private JButton[][] boardButtons = new JButton[8][8];
    private String[][] boardState;
    private Point selectedPiece = null;
    private final AtomicBoolean waitingForResponse = new AtomicBoolean(false);
    private String moveResponse = null;
    private String turnResponse = null;

    public ChessBoardFrame(ChessClient client, String playerColor) {
        this.client = client;
        this.playerColor = playerColor;

        setTitle("Chess Game - You are " + playerColor);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        initializeBoard();
        listenForUpdates();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeBoard() {
        try {
            boardState = client.receiveInitialBoard();

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    JButton square = new JButton();
                    square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                    square.setFont(new Font("Segoe UI Symbol", Font.BOLD, 40));
                    square.setFocusPainted(false);

                    String piece = boardState[row][col];
                    if (piece != null) {
                        square.setText(piece);
                    }

                    int finalRow = row;
                    int finalCol = col;
                    square.addActionListener(e -> handleMove(finalRow, finalCol));

                    boardButtons[row][col] = square;
                    add(square);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error initializing the board.");
        }
    }

    private void handleMove(int row, int col) {
        try {
            waitingForResponse.set(true);
            turnResponse = null;
            client.sendMessage("GET_TURN");

            for (int i = 0; i < 20 && turnResponse == null; i++) {
                Thread.sleep(100);
            }

            waitingForResponse.set(false);

            if (turnResponse == null) {
                JOptionPane.showMessageDialog(this, "No response from server when checking turn.");
                return;
            }

            if (!turnResponse.equals(playerColor)) {
                JOptionPane.showMessageDialog(this, "It's not your turn!");
                return;
            }

            if (selectedPiece == null) {
                if (boardState[row][col] != null && isPlayerPiece(boardState[row][col])) {
                    selectedPiece = new Point(row, col);
                    boardButtons[row][col].setBackground(Color.YELLOW);
                } else {
                    JOptionPane.showMessageDialog(this, "You can only select your own pieces!");
                }
            } else {
                if (selectedPiece.x == row && selectedPiece.y == col) {
                    boardButtons[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                    selectedPiece = null;
                } else {
                    moveResponse = null;
                    waitingForResponse.set(true);

                    client.sendMessage("MOVE " + selectedPiece.x + " " + selectedPiece.y + " " + row + " " + col);

                    final int selectedRow = selectedPiece.x;
                    final int selectedCol = selectedPiece.y;
                    boardButtons[selectedRow][selectedCol].setBackground(
                            (selectedRow + selectedCol) % 2 == 0 ? Color.WHITE : Color.GRAY);

                    for (int i = 0; i < 30 && moveResponse == null; i++) {
                        Thread.sleep(100);
                    }

                    waitingForResponse.set(false);

                    if (moveResponse == null) {
                        JOptionPane.showMessageDialog(this, "No response from server. Try again.");
                    } else if (!"MOVE_SUCCESS".equals(moveResponse)) {
                        JOptionPane.showMessageDialog(this, "Invalid move!");
                    }

                    selectedPiece = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing move.");
            if (selectedPiece != null) {
                boardButtons[selectedPiece.x][selectedPiece.y].setBackground(
                        (selectedPiece.x + selectedPiece.y) % 2 == 0 ? Color.WHITE : Color.GRAY);
                selectedPiece = null;
            }
            waitingForResponse.set(false);
        }
    }

    private boolean isPlayerPiece(String piece) {
        return (playerColor.equals("white") && piece.matches("♙|♖|♘|♗|♕|♔")) ||
                (playerColor.equals("black") && piece.matches("♟|♜|♞|♝|♛|♚"));
    }

    private void listenForUpdates() {
        new Thread(() -> {
            try {
                while (true) {
                    String message = client.receiveMessage();
                    if (message == null) {
                        break;
                    }

                    if (waitingForResponse.get()) {
                        if (message.equals("MOVE_SUCCESS") || message.equals("MOVE_INVALID")) {
                            moveResponse = message;
                            continue;
                        } else if (message.equals("white") || message.equals("black")) {
                            turnResponse = message;
                            continue;
                        }
                    }

                    if (message.startsWith("CHECKMATE")) {
                        final String winner = message.substring("CHECKMATE ".length());
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this,
                                    "CHECKMATE! " + winner + " wins the game!");
                            dispose();
                            new ChessGameFrame(client);
                        });
                        break;
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            if (message.startsWith("UPDATE_BOARD")) {
                                String boardData = message.substring("UPDATE_BOARD ".length());
                                if (client.getLastCheckMessage() == null ||
                                        !client.getLastCheckMessage().startsWith("CHECK")) {
                                    client.clearCheckMessage();
                                }
                                updateBoardState(boardData);
                            } else if (message.startsWith("ERROR")) {
                                JOptionPane.showMessageDialog(this, message.substring("ERROR ".length()));
                            } else if (message.startsWith("CHECK")) {
                                client.setLastCheckMessage(message);
                                String colorInCheck = message.substring("CHECK ".length());
                                highlightKingInCheck(colorInCheck);
                                JOptionPane.showMessageDialog(this,
                                        colorInCheck + " king is in CHECK!");
                            }
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Connection to server lost.")
                );
            }
        }).start();
    }

    private void highlightKingInCheck(String colorInCheck) {
        String kingPiece = colorInCheck.equals("white") ? "♔" : "♚";

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (kingPiece.equals(boardState[r][c])) {
                    boardButtons[r][c].setBackground(Color.RED);
                    return;
                }
            }
        }
    }

    private void updateBoardState(String boardData) {
        String[] rows = boardData.split(";");
        for (int i = 0; i < 8; i++) {
            String[] cols = rows[i].split(",", -1);
            for (int j = 0; j < 8; j++) {
                boardState[i][j] = cols[j].isEmpty() ? null : cols[j];
                boardButtons[i][j].setText(boardState[i][j] == null ? "" : boardState[i][j]);

                if ((selectedPiece == null || selectedPiece.x != i || selectedPiece.y != j) &&
                        !isKingPosition(i, j)) {
                    boardButtons[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                }
            }
        }
    }

    private boolean isKingPosition(int row, int col) {
        String kingInCheck = null;

        if (client.getLastCheckMessage() != null && client.getLastCheckMessage().startsWith("CHECK")) {
            kingInCheck = client.getLastCheckMessage().substring("CHECK ".length());
        }

        if (kingInCheck != null && boardState[row][col] != null) {
            String kingPiece = kingInCheck.equals("white") ? "♔" : "♚";
            return kingPiece.equals(boardState[row][col]);
        }
        return false;
    }
}
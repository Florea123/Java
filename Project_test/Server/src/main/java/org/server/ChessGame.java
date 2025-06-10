package org.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.server.exceptions.InvalidMoveException;

public class ChessGame {
    private final List<String> players = new ArrayList<>(2);
    private final String[][] board;
    private boolean isGameReady;
    private String whitePlayer;
    private String blackPlayer;
    private String currentTurn;
    private String kingInCheck = null;

    public ChessGame(String player1) {
        this.board = initializeBoard();
        this.isGameReady = false;
        this.currentTurn = "white";
        addPlayer(player1);
    }

    public synchronized void addPlayer(String player) {
        if (!isFull()) {
            if (players.isEmpty()) {
                whitePlayer = player;
            } else {
                blackPlayer = player;
                isGameReady = true;
            }
            players.add(player);
        }
    }

    public synchronized boolean isFull() {
        return players.size() == 2;
    }

    public synchronized boolean isGameReady() {
        return isGameReady;
    }

    public synchronized String getCurrentTurn() {
        return currentTurn;
    }

    public synchronized void switchTurn() {
        currentTurn = currentTurn.equals("white") ? "black" : "white";
        System.out.println("Turn switched to: " + currentTurn);
    }

    public synchronized boolean canPlayerMove(String player) {
        return (currentTurn.equals("white") && player.equals(whitePlayer)) ||
                (currentTurn.equals("black") && player.equals(blackPlayer));
    }

    public synchronized String getPlayerColor(String player) {
        if (player.equals(whitePlayer)) {
            return "white";
        } else if (player.equals(blackPlayer)) {
            return "black";
        }
        return null;
    }

    private String[][] initializeBoard() {
        String[][] initialBoard = new String[8][8];
        initialBoard[7] = new String[]{"♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"};
        initialBoard[6] = new String[]{"♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"};
        initialBoard[1] = new String[]{"♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"};
        initialBoard[0] = new String[]{"♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"};
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                initialBoard[i][j] = null;
            }
        }
        return initialBoard;
    }

    public synchronized String[][] getBoard() {
        return board;
    }

    public synchronized List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public synchronized String getWhitePlayer() {
        return whitePlayer;
    }

    public synchronized String getBlackPlayer() {
        return blackPlayer;
    }

    public synchronized boolean isKingInCheck(String color) {
        int kingRow = -1;
        int kingCol = -1;
        String kingPiece = color.equals("white") ? "♔" : "♚";

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (kingPiece.equals(board[r][c])) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                String piece = board[r][c];
                if (piece != null && isOpponentPiece(kingPiece, piece)) {
                    if (isValidMove(piece, r, c, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public synchronized boolean movePiece(String player, int fromRow, int fromCol, int toRow, int toCol) {
        if (!canPlayerMove(player)) {
            return false;
        }

        String piece = board[fromRow][fromCol];
        if (piece == null || !isValidMove(piece, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        String originalTarget = board[toRow][toCol];
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;

        if ((piece.equals("♙") && toRow == 0) || (piece.equals("♟") && toRow == 7)) {
            board[toRow][toCol] = piece.equals("♙") ? "♕" : "♛";
        }

        String playerColor = getPlayerColor(player);
        boolean kingInCheck = isKingInCheck(playerColor);

        if (kingInCheck) {
            board[fromRow][fromCol] = piece;
            board[toRow][toCol] = originalTarget;
            return false;
        }

        String opponentColor = playerColor.equals("white") ? "black" : "white";
        if (isKingInCheck(opponentColor)) {
            this.kingInCheck = opponentColor;

            if (isCheckmate(opponentColor)) {
                return true;
            }
        } else {
            this.kingInCheck = null;
        }

        return true;
    }

    public boolean isCheckmate(String color) {
        for (int r1 = 0; r1 < 8; r1++) {
            for (int c1 = 0; c1 < 8; c1++) {
                String piece = board[r1][c1];

                if (piece == null ||
                        (color.equals("white") && !piece.matches("♔|♕|♖|♗|♘|♙")) ||
                        (color.equals("black") && !piece.matches("♚|♛|♜|♝|♞|♟"))) {
                    continue;
                }

                for (int r2 = 0; r2 < 8; r2++) {
                    for (int c2 = 0; c2 < 8; c2++) {
                        if (isValidMove(piece, r1, c1, r2, c2)) {
                            String originalTarget = board[r2][c2];
                            board[r2][c2] = piece;
                            board[r1][c1] = null;

                            boolean stillInCheck = isKingInCheck(color);

                            board[r1][c1] = piece;
                            board[r2][c2] = originalTarget;

                            if (!stillInCheck) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public synchronized String getKingInCheck() {
        return kingInCheck;
    }

    private boolean isValidMove(String piece, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow == toRow && fromCol == toCol) {
            System.out.println("Invalid move: Same position");
            return false;
        }

        String targetPiece = board[toRow][toCol];
        boolean isCapture = targetPiece != null && isOpponentPiece(piece, targetPiece);

        if (targetPiece != null && !isCapture) {
            System.out.println("Invalid move: Cannot capture own piece");
            return false;
        }

        boolean result = false;
        switch (piece) {
            case "♙":
                result = isValidPawnMove(piece, fromRow, fromCol, toRow, toCol, isCapture, true);
                break;
            case "♟":
                result = isValidPawnMove(piece, fromRow, fromCol, toRow, toCol, isCapture, false);
                break;
            case "♖": case "♜":
                result = isValidRookMove(fromRow, fromCol, toRow, toCol);
                break;
            case "♘": case "♞":
                result = isValidKnightMove(fromRow, fromCol, toRow, toCol);
                break;
            case "♗": case "♝":
                result = isValidBishopMove(fromRow, fromCol, toRow, toCol);
                break;
            case "♕": case "♛":
                result = isValidQueenMove(fromRow, fromCol, toRow, toCol);
                break;
            case "♔": case "♚":
                result = isValidKingMove(fromRow, fromCol, toRow, toCol);
                break;
            default:
                result = false;
        }

        System.out.println("Move from " + fromRow + "," + fromCol + " to " + toRow + "," + toCol +
                " for piece " + piece + ": " + (result ? "valid" : "invalid"));
        return result;
    }

    private boolean isOpponentPiece(String piece, String targetPiece) {
        return (piece.matches("♙|♖|♘|♗|♕|♔") && targetPiece.matches("♟|♜|♞|♝|♛|♚")) ||
                (piece.matches("♟|♜|♞|♝|♛|♚") && targetPiece.matches("♙|♖|♘|♗|♕|♔"));
    }

    private boolean isValidPawnMove(String piece, int fromRow, int fromCol, int toRow, int toCol, boolean isCapture, boolean isWhite) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        if (isCapture) {
            return (toRow - fromRow) == direction && Math.abs(fromCol - toCol) == 1;
        }
        else {
            if (fromCol == toCol) {
                if ((toRow - fromRow) == direction) {
                    return board[toRow][toCol] == null;
                }
                else if (fromRow == startRow && (toRow - fromRow) == 2 * direction) {
                    int middleRow = fromRow + direction;
                    return board[toRow][toCol] == null && board[middleRow][toCol] == null;
                }
            }
        }
        return false;
    }

    private boolean isValidRookMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }
        return isPathClear(fromRow, fromCol, toRow, toCol);
    }

    private boolean isValidKnightMove(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private boolean isValidBishopMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol)) {
            return false;
        }
        return isPathClear(fromRow, fromCol, toRow, toCol);
    }

    private boolean isValidQueenMove(int fromRow, int fromCol, int toRow, int toCol) {
        return isValidRookMove(fromRow, fromCol, toRow, toCol) || isValidBishopMove(fromRow, fromCol, toRow, toCol);
    }

    private boolean isValidKingMove(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);
        return rowDiff <= 1 && colDiff <= 1;
    }

    private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);

        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        while (currentRow != toRow || currentCol != toCol) {
            if (board[currentRow][currentCol] != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }
}
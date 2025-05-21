package org.example;

public class HexGame {
    private String gameId;
    private Player player1;
    private Player player2;
    private String[][] board;
    private int boardSize;
    private boolean isGameOver;
    private Player currentPlayer;
    private boolean gameStarted;

    public HexGame(String gameId) {
        this.gameId = gameId;
        this.boardSize = 11;
        this.board = new String[boardSize][boardSize];
        this.isGameOver = false;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = "\u26AA";
            }
        }
    }

    public void addPlayer(Player player) {
        if (player1 == null) {
            player1 = player;
            currentPlayer = player1;
        } else if (player2 == null) {
            player2 = player;
            startGame();
        } else {
            throw new IllegalStateException("Game is full");
        }
    }

    private void startGame() {
        currentPlayer = player1;
        gameStarted = true;
        player1.sendMessage("The game has started! You are playing as " + player1.getColor());
        player2.sendMessage("The game has started! You are playing as " + player2.getColor());
        currentPlayer.sendMessage("\nIt's your turn! Current board state:\n" + displayBoard());
        player1.setTimeExpiredCallback(() -> handleTimeExpired(player1));
        player2.setTimeExpiredCallback(() -> handleTimeExpired(player2));
        player1.startTimer();
    }

    public boolean submitMove(Player player, int x, int y) {
        if(!gameStarted) {
            throw new IllegalStateException("Game has not started yet");
        }
        if (isGameOver) {
            throw new IllegalStateException("Game is already over");
        }
        if (player != currentPlayer) {
            throw new IllegalStateException("It's not your turn");
        }
        if (board[x][y] != "\u26AA") {
            throw new IllegalStateException("Cell is already occupied");
        }
        if(player.getColor() == "red") {
            board[x][y] = "\uD83D\uDD34";
        } else if(player.getColor() == "blue") {
            board[x][y] = "\uD83D\uDD35";
        } else {
            throw new IllegalArgumentException("Invalid player color");
        }
        if (checkWinCondition(player)) {
            isGameOver = true;
            currentPlayer.stopTimer();
            player.sendMessage("You win!");
            player2.sendMessage("You lose!");
            return true;
        }
        switchTurn();
        return false;
    }

    private void switchTurn() {
        currentPlayer.stopTimer();
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        currentPlayer.startTimer();
        currentPlayer.sendMessage("It's your turn! Current board state:\n" + displayBoard());
        currentPlayer.sendMessage(getAvailableMoves());
    }

    private boolean checkWinCondition(Player player) {
        String targetColor = player.getColor().equals("blue") ? "\uD83D\uDD35" : "\uD83D\uDD34";
        boolean[][] visited = new boolean[boardSize][boardSize];

        if (player.getColor().equals("blue")) {
            for (int i = 0; i < boardSize; i++) {
                if (board[i][0].equals(targetColor) && dfs(i, 0, visited, targetColor, "blue")) {
                    return true;
                }
            }
        } else if (player.getColor().equals("red")) {
            for (int j = 0; j < boardSize; j++) {
                if (board[0][j].equals(targetColor) && dfs(0, j, visited, targetColor, "red")) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs(int x, int y, boolean[][] visited, String targetColor, String playerColor) {
        if (playerColor.equals("blue") && y == boardSize - 1) {
            return true;
        }
        if (playerColor.equals("red") && x == boardSize - 1) {
            return true;
        }

        visited[x][y] = true;

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 6; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (newX >= 0 && newX < boardSize && newY >= 0 && newY < boardSize &&
                    !visited[newX][newY] && board[newX][newY].equals(targetColor)) {
                if (dfs(newX, newY, visited, targetColor, playerColor)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String displayBoard() {
        StringBuilder boardDisplay = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == null) {
                    boardDisplay.append(". ");
                } else {
                    boardDisplay.append(board[i][j]).append(" ");
                }
            }
            boardDisplay.append("\n");
        }
        return boardDisplay.toString();
    }

    public String getAvailableMoves() {
        StringBuilder availableMoves = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == "\u26AA") {
                    availableMoves.append("(").append(i).append(", ").append(j).append(") ");
                }
            }
        }
        return availableMoves.toString().trim();
    }

    private void handleTimeExpired(Player player) {
        isGameOver = true;
        currentPlayer.stopTimer();
        Player winner = (player == player1) ? player2 : player1;
        System.out.println("Player " + player.getColor() + " ran out of time. Player " + winner.getColor() + " wins!");
    }


    public String getGameId() {
        return gameId;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }



}
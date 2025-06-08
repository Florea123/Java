package org.client;

import java.io.*;
import java.net.Socket;

public class ChessClient {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String username;
    private String lastCheckMessage = null;


    public ChessClient(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public String[][] receiveInitialBoard() throws IOException {
        sendMessage("GET_BOARD");
        String response = receiveMessage();
        System.out.println("Received Board State: " + response);

        String[] rows = response.split(";");
        if (rows.length != 8) {
            throw new IllegalStateException("Invalid board state: Incorrect number of rows");
        }

        String[][] board = new String[8][8];
        for (int i = 0; i < 8; i++) {
            String[] cols = rows[i].split(",", -1);
            if (cols.length != 8) {
                throw new IllegalStateException("Invalid board state: Incorrect number of columns in row " + i);
            }
            for (int j = 0; j < 8; j++) {
                board[i][j] = cols[j].isEmpty() ? null : cols[j];
            }
        }
        return board;
    }

    public String getLastCheckMessage() {
        return lastCheckMessage;
    }

    public void setLastCheckMessage(String message) {
        if (message == null) {
            this.lastCheckMessage = null;
        } else if (message.startsWith("UPDATE_BOARD") && lastCheckMessage != null) {
        } else if (message.startsWith("CHECK") || message.startsWith("CHECKMATE")) {
            this.lastCheckMessage = message;
        }
    }

    public void clearCheckMessage() {
        this.lastCheckMessage = null;
    }

    public int getPlayerScore(String username) throws IOException {
        sendMessage("GET_SCORE " + username);
        String response = receiveMessage();
        try {
            return Integer.parseInt(response);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void clearMessageQueue() {
        try {
            while (in.ready()) {
                in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
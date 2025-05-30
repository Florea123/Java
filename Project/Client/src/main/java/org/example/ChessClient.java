package org.example;

import java.io.*;
import java.net.Socket;

public class ChessClient {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

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
}
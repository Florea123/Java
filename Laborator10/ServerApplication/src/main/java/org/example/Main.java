package org.example;

public class Main {
    public static void main(String[] args) {
        int port = 12346;
        GameServer server = new GameServer(port);
        server.start();
    }
}

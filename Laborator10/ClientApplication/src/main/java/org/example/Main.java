package org.example;
public class Main {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12346;
        GameClient client = new GameClient(serverAddress, serverPort);
        client.start();
    }
}
package org.server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final DatabaseManager dbManager = new DatabaseManager();

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
                }
            }
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
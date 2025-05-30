package org.example;

import javax.swing.SwingUtilities;

public class ClientApp {
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                ChessClient client = new ChessClient("localhost", 12345);
                new org.example.LoginFrame(client); // Inițializează interfața de login
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
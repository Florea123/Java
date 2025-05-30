package org.client;

import javax.swing.SwingUtilities;

public class ClientApp {
    public void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                ChessClient client = new ChessClient("localhost", 12345);
                new LoginFrame(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
package org.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class WaitingFrame extends JFrame {
    private final ChessClient client;
    private volatile boolean isWaiting = true;

    public WaitingFrame(ChessClient client) {
        this.client = client;

        setTitle("Waiting for Player");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel waitingLabel = new JLabel("Waiting for another player to join...");
        waitingLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(waitingLabel);

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.addActionListener((ActionEvent e) -> {
            isWaiting = false;
            client.sendMessage("EXIT_LOBBY " + client.getUsername());
        });
        add(exitButton);

        setLocationRelativeTo(null);
        setVisible(true);

        new Thread(() -> {
            try {
                while (isWaiting) {
                    String message = client.receiveMessage();

                    if (message == null) {
                        break;
                    }

                    if (message.startsWith("GAME_READY")) {
                        String[] parts = message.split(" ");
                        if (parts.length > 1) {
                            String color = parts[1];
                            SwingUtilities.invokeLater(() -> {
                                isWaiting = false;
                                dispose();
                                new ChessBoardFrame(client, color);
                            });
                            break;
                        }
                    } else if (message.equals("EXIT_LOBBY_SUCCESS")) {
                        SwingUtilities.invokeLater(() -> {
                            isWaiting = false;
                            dispose();
                            new ChessGameFrame(client);
                        });
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
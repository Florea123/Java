package org.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ChessGameFrame extends JFrame {
    private final ChessClient client;

    public ChessGameFrame(ChessClient client) {
        this.client = client;

        setTitle("Chess Game - Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // Add padding

        JLabel titleLabel = new JLabel("Chess Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        JButton findGameButton = createStyledButton("Find Game");
        JButton logoutButton = createStyledButton("Logout");
        JButton exitButton = createStyledButton("Exit");

        findGameButton.addActionListener((ActionEvent e) -> {
            client.sendMessage("FIND_GAME " + client.getUsername());
            new WaitingFrame(client);
            dispose();
        });

        logoutButton.addActionListener((ActionEvent e) -> {
            try {
                client.sendMessage("LOGOUT " + client.getUsername());
                String response = client.receiveMessage();
                if ("LOGOUT_SUCCESS".equals(response)) {
                    client.setUsername(null);
                    dispose();
                    new LoginFrame(client);
                } else {
                    JOptionPane.showMessageDialog(this, "Logout failed: " + response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exitButton.addActionListener((ActionEvent e) -> {
            try {
                client.sendMessage("EXIT " + client.getUsername());
                String response = client.receiveMessage();
                if ("EXIT_SUCCESS".equals(response)) {
                    client.getSocket().close();
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Exit failed: " + response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        buttonPanel.add(titleLabel);
        buttonPanel.add(findGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
        buttonPanel.add(logoutButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setPreferredSize(new Dimension(200, 50));

        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        return button;
    }
}
package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginFrame(ChessClient client) {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Username:", SwingConstants.CENTER));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(usernameField);

        add(new JLabel("Password:", SwingConstants.CENTER));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        add(registerButton);

        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        add(statusLabel);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            client.sendMessage("LOGIN " + username + " " + password);
            try {
                String response = client.receiveMessage();
                if ("LOGIN_SUCCESS".equals(response)) {
                    new ChessGameFrame();
                    dispose();
                } else {
                    statusLabel.setText(response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        registerButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            client.sendMessage("REGISTER " + username + " " + password);
            try {
                String response = client.receiveMessage();
                if ("REGISTER_SUCCESS".equals(response)) {
                    statusLabel.setText("Registration successful! Please log in.");
                } else {
                    statusLabel.setText(response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
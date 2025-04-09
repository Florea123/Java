package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel{
    final MainFrame frame;
    JButton exitBtn = new JButton("Exit");
    JButton newGameBtn = new JButton("New Game");
    JButton saveGameBtn = new JButton("Save Game");
    JButton loadGameBtn = new JButton("Load Game");
    JButton exportImageBtn = new JButton("Export Image");

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();

        add(newGameBtn);
        add(saveGameBtn);
        add(loadGameBtn);
        add(exportImageBtn);
        add(exitBtn);
    }

    private void init()
    {
        setLayout(new GridLayout(1,4));
        exitBtn.addActionListener(this::exitGame);
        newGameBtn.addActionListener(this::newGame);
        saveGameBtn.addActionListener(this::saveGame);
        loadGameBtn.addActionListener(this::loadGame);
        exportImageBtn.addActionListener(this::exportImage);
    }
    private void exitGame(ActionEvent e)
    {
        frame.dispose();
    }
    private void newGame(ActionEvent e)
    {
        frame.canvas.clearLines();
        frame.canvas.clearDots();
        frame.canvas.resetScores();
        frame.configPanel.updateScore(0, 0);
        frame.canvas.createOffScreenImage();
        frame.canvas.repaint();
    }
    private void saveGame(ActionEvent e) {
        ObjectMapper mapper = new ObjectMapper();
        GameState state = frame.canvas.getGameState();
        try {
            mapper.writeValue(new File("C:\\Users\\Andrei\\Desktop\\game_state.json"), state);
            System.out.println("Game state saved to game_state.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Failed to save game state.");
        }
    }

    private void loadGame(ActionEvent e) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            GameState state = mapper.readValue(new File("C:\\Users\\Andrei\\Desktop\\game_state.json"), GameState.class);
            frame.canvas.setGameState(state);
            System.out.println("Game state loaded from game_state.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Failed to load game state.");
        }
    }
    private void exportImage(ActionEvent e) {
        String filePath = "C:\\Users\\Andrei\\Desktop\\exported_game_board.png";
        frame.canvas.saveImage(filePath);
        System.out.println("Image exported to: " + filePath);
    }
}

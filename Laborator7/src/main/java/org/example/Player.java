package org.example;

import java.util.List;
public class Player implements Runnable{
    private String name;
    private Game game;
    private boolean running;

    public Player(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    private boolean submitWord()
    {
        List<Tile> extracted = game.getBag().extractTiles(7);
        if(extracted.isEmpty())
            return false;
        StringBuilder wordBuilder = new StringBuilder();
        for (Tile tile : extracted) {
            wordBuilder.append(tile.getLetter());
        }
        String word = wordBuilder.toString();
        game.getBoard().addWord(this, word);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return true;
    }
    @Override
    public void run() {
        running = true;
        while (running) {
            if (!submitWord()) {
                running = false;
            }
        }
    }
}

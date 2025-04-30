package org.example;

import java.util.List;

public class Player implements Runnable {
    private String name;
    private Game game;
    private boolean running;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private boolean submitWord() {
        synchronized (game) {
            while (!isMyTurn()) {
                try {
                    game.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }

            System.out.println(name + "'s turn:");
            List<Tile> extracted = game.getBag().extractTiles(7);
            if (extracted.isEmpty()) {
                System.out.println(name + " cannot extract tiles. The bag is empty.");
                running = false;
                game.notifyAll();
                return false;
            }

            System.out.print(name + "'s letters: ");
            for (Tile tile : extracted) {
                System.out.print(tile.getLetter() + " ");
            }
            System.out.println();

            StringBuilder wordBuilder = new StringBuilder();
            int wordScore = 0;

            for (Tile tile : extracted) {
                wordBuilder.append(tile.getLetter());
                wordScore += tile.getPoints();
            }

            String word = wordBuilder.toString();
            if (game.getDictionary().isWord(word)) {
                game.getBoard().addWord(this, word);
                score += wordScore;
                System.out.println(name + " created the word: " + word + " (+" + wordScore + " points)");
            } else {
                System.out.println(name + " could not create a valid word. Discarding tiles.");
            }

            game.nextTurn();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return true;
    }

    private boolean isMyTurn() {
        return game.getCurrentPlayerIndex() == game.getPlayers().indexOf(this);
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
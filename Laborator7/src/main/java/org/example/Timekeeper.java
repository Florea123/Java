package org.example;

public class Timekeeper implements Runnable {
    private final Game game;
    private final int timeLimit;

    public Timekeeper(Game game, int timeLimit) {
        this.game = game;
        this.timeLimit = timeLimit;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while (true) {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Elapsed time: " + elapsedTime + " seconds");

            if (elapsedTime >= timeLimit) {
                System.out.println("Time limit exceeded! Stopping the game...");
                synchronized (game) {
                    game.notifyAll();
                }
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
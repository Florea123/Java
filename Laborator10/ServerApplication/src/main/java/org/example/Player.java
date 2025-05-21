package org.example;

import java.util.concurrent.Executors;
import java.io.PrintWriter;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Player {
    private String color;
    private int remainingTime;
    private ScheduledExecutorService timer;
    private PrintWriter out;
    private Runnable timeExpiredCallback;

    public Player(String color, int initialTime, PrintWriter out) {
        this.color = color;
        this.remainingTime = initialTime;
        this.out = out;
    }

    public void startTimer() {
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(() -> {
            decrementTime(1);
            if (isOutOfTime()) {
                stopTimer();
                if (timeExpiredCallback != null) {
                    timeExpiredCallback.run();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void stopTimer() {
        if (timer != null && !timer.isShutdown()) {
            timer.shutdown();
        }
    }

            public void decrementTime(int seconds) {
        remainingTime -= seconds;
        if (remainingTime <= 0) {
            remainingTime = 0;
        }
    }

    public boolean isOutOfTime() {
        return remainingTime <= 0;
    }

    public String getColor() {
        return color;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    public void setTimeExpiredCallback(Runnable callback) {
        this.timeExpiredCallback = callback;
    }
}
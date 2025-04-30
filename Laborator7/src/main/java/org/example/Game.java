package org.example;
import java.util.ArrayList;
import java.util.List;
public class Game {
    private Bag bag=new Bag();
    private Board board=new Board();
    private MockDictionary dictionary= new MockDictionary("src/main/resources/dictionary.txt");
    private List<Player> players =new ArrayList<>();
    private int currentPlayerIndex;

    public void addPlayer(Player player)
    {
        players.add(player);
        player.setGame(this);
    }
    public Bag getBag()
    {
        return bag;
    }
    public Board getBoard()
    {
        return board;
    }
    public MockDictionary getDictionary()
    {
        return dictionary;
    }
    public synchronized int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }
    public synchronized void nextTurn()
    {
        currentPlayerIndex++;
        if(currentPlayerIndex>=players.size())
            currentPlayerIndex=0;
        try {
            System.out.println("Waiting for the next player's turn...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        notifyAll();
    }
    public List<Player> getPlayers()
    {
        return players;
    }
    public void play() {
        Thread timekeeperThread = new Thread(new Timekeeper(this, 200));
        timekeeperThread.setDaemon(true);
        timekeeperThread.start();

        List<Thread> playerThreads = new ArrayList<>();
        for (Player player : players) {
            Thread playerThread = new Thread(player);
            playerThreads.add(playerThread);
            playerThread.start();
        }

        for (Thread thread : playerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        determineWinner();
    }

    private void determineWinner() {
        Player winner = null;
        int highestScore = 0;

        for (Player player : players) {
            System.out.println(player.getName() + " scored: " + player.getScore());
            if (player.getScore() > highestScore) {
                highestScore = player.getScore();
                winner = player;
            }
        }

        if (winner != null) {
            System.out.println("The winner is " + winner.getName() + " with a score of " + highestScore + "!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}

package org.example;
import java.util.ArrayList;
import java.util.List;
public class Game {
    private Bag bag=new Bag();
    private Board board=new Board();
    private MockDictionary dictionary= new MockDictionary();
    private List<Player> players =new ArrayList<>();

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
    public void play()
    {
        for(Player player: players)
        {
            Thread playerThread = new Thread(player);
            playerThread.start();
        }
    }
}

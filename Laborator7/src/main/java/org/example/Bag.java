package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class Bag {
    private List<Tile> letters = new ArrayList<Tile>();

    public Bag() {
        Random random = new Random();
        for(char c='a';c<='z';c++)
        {
            for(int i=0;i<10;i++)
            {
                int points = random.nextInt(1, 10);
                letters.add(new Tile(c,points));
            }
        }
        Collections.shuffle(letters);
    }
    public synchronized List<Tile> extractTiles(int howMany) {
        List<Tile> extracted = new ArrayList<Tile>();
        for(int i=0;i<howMany;i++)
        {
            if(letters.isEmpty())
                break;
            extracted.add(letters.remove(0));
        }
        return extracted;
    }
}

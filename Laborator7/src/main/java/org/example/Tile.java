package org.example;

public class Tile {
    private char letter;
    private int points;

    public Tile(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }
    public char getLetter() {
        return letter;
    }
    public int getPoints() {
        return points;
    }
    public void setLetter(char letter) {
        this.letter = letter;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    @Override
    public String toString() {
        return "Tile{" +
                "letter=" + letter +
                ", points=" + points +
                '}';
    }

}

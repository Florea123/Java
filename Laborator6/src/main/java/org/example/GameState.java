package org.example;

import java.awt.*;
import java.util.List;

public class GameState {
    public List<Point> dots;
    public List<Line> lines;
    public double redScore;
    public double blueScore;

    public static class Line {
        public Point start;
        public Point end;

        public Line() {}

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
    }
}
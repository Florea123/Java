package org.example;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;
    private int canvasWidth = 600;
    private int canvasHeight = 600;
    private BufferedImage image;
    private Graphics2D offscreen;
    private List<Point> dots;
    private List<Line> lines;
    private boolean isRedTurn =true;
    private Point firstDot =null;
    private Random random = new Random();
    private double redScore=0;
    private double blueScore=0;

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        setBorder(BorderFactory.createEtchedBorder());
        this.dots = new ArrayList<>();
        this.lines = new ArrayList<>();
        createOffScreenImage();
        initMouseListener();
    }

    public void createOffScreenImage() {
        image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        offscreen = image.createGraphics();
        offscreen.setColor(Color.WHITE);
        offscreen.fillRect(0, 0, canvasWidth, canvasHeight);
        paintDots();
        paintLines();
    }

    private void paintDots() {
        int dotSize = 10;
        int numberOfDots = (int) frame.configPanel.spinner.getValue();
        offscreen.setColor(Color.BLACK);
        dots.clear();
        for (int i = 0; i < numberOfDots; i++) {
            int x=-1;
            int y=-1;
            while(x<10 || y<10 || x>canvasWidth-10 || y>canvasHeight-10) {
                x = (int) (Math.random() * canvasWidth);
                y = (int) (Math.random() * canvasHeight);
            }
            dots.add(new Point(x, y));
            if(i%2==0) {
                offscreen.setColor(Color.RED);
            } else {
                offscreen.setColor(Color.BLUE);
            }
            offscreen.fillOval(x, y, dotSize, dotSize);
        }
    }
    private void paintDotsSave() {
        int dotSize = 10;
        offscreen.setColor(Color.BLACK);
        for (int i = 0; i < dots.size(); i++) {
            Point dot = dots.get(i);
            if (i % 2 == 0) {
                offscreen.setColor(Color.RED);
            } else {
                offscreen.setColor(Color.BLUE);
            }
            offscreen.fillOval(dot.x, dot.y, dotSize, dotSize);
        }
    }
    private void paintLines() {
        int dotSize = 10;
        for (Line line : lines) {
            int startX = line.start.x + dotSize / 2;
            int startY = line.start.y + dotSize / 2;
            int endX = line.end.x + dotSize / 2;
            int endY = line.end.y + dotSize / 2;

            if (dots.indexOf(line.start) % 2 == 0) {
                offscreen.setColor(Color.RED);
            } else {
                offscreen.setColor(Color.BLUE);
            }
            offscreen.drawLine(startX, startY, endX, endY);
        }
    }

    private void initMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickedPoint = new Point(e.getX(), e.getY());
                Point selectedDot = findDot(clickedPoint);
                if (selectedDot != null) {
                    if (firstDot == null) {
                        firstDot = selectedDot;
                    } else {
                        if (isSameColor(firstDot, selectedDot)) {
                            lines.add(new Line(firstDot, selectedDot));
                            double distance = firstDot.distance(selectedDot);
                            if (isRedTurn) {
                                redScore += distance;
                            } else {
                                blueScore += distance;
                            }
                            updateScores();
                            paintLines();
                            isRedTurn = !isRedTurn;
                            firstDot = null;
                            repaint();
                        } else {
                            firstDot = selectedDot;
                        }
                    }
                }
            }
        });
    }
    private Point findDot(Point clickedPoint) {
        for (Point dot : dots) {
            if (clickedPoint.distance(dot) < 10) {
                return dot;
            }
        }
        return null;
    }
    private boolean isSameColor(Point dot1, Point dot2) {
        int index1 = dots.indexOf(dot1);
        int index2 = dots.indexOf(dot2);
        return (index1 % 2 == 0 && index2 % 2 == 0) || (index1 % 2 != 0 && index2 % 2 != 0);
    }
    private void updateScores() {
        frame.configPanel.updateScore(redScore, blueScore);
    }
    public void clearLines()
    {
        lines.clear();
    }
    public void clearDots() {
        dots.clear();
    }
    public void resetScores() {
        redScore = 0;
        blueScore = 0;
    }
    public void saveImage(String filePath) {
        try {
            ImageIO.write(image, "PNG", new File(filePath));
            System.out.println("Game board saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save the game board.");
        }
    }
    public GameState getGameState() {
        GameState state = new GameState();
        state.dots = new ArrayList<>();
        state.lines = new ArrayList<>();
        for(Point dot : dots) {
            state.dots.add(new Point(dot));
        }
        for (Line line : lines) {
            state.lines.add(new GameState.Line(line.start, line.end));
        }
        state.redScore = redScore;
        state.blueScore = blueScore;
        return state;
    }

    public void setGameState(GameState state) {
        dots = new ArrayList<>();
        lines = new ArrayList<>();
        for(Point dot : state.dots) {
            dots.add(new Point(dot));
        }
        for (GameState.Line line : state.lines) {
            lines.add(new Line(line.start, line.end));
        }
        redScore = state.redScore;
        blueScore = state.blueScore;

        offscreen.setColor(Color.WHITE);
        offscreen.fillRect(0, 0, canvasWidth, canvasHeight);
        paintDotsSave();
        paintLines();
        repaint();
    }
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.drawImage(image, 0, 0, this);
    }
    private static class Line {
        Point start;
        Point end;

        Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
    }
}
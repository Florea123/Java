package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        /*
        Image img1 = new Image("Image1", "C:\Users\Andrei\Desktop\Poze\image1.jpg");

        repository.add(img1);
        repository.displayImage("Image1");
        */
        Shell shell = new Shell();
        try {
            shell.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
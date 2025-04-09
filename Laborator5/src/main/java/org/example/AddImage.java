package org.example;

import java.io.BufferedReader;
import java.io.IOException;

public class AddImage implements Command {
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        System.out.println("Enter the name of the image:");
        String name = reader.readLine();
        System.out.println("Enter the file path of the image:");
        String filePath = reader.readLine();
        Image img = new Image(name, filePath);
        repo.add(img);
        System.out.println("Image added.");
    }
}

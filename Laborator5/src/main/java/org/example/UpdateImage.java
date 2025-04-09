package org.example;

import java.io.BufferedReader;
import java.io.IOException;

public class UpdateImage implements Command {
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        System.out.println("Enter the name of the image you want to update:");
        String name = reader.readLine();
        System.out.println("Enter the new name of the image:");
        String newName = reader.readLine();
        System.out.println("Enter the new file path of the image:");
        String filePath = reader.readLine();
        repo.update(name, newName,filePath);
        System.out.println("Image updated.");
    }
}

package org.example;

import java.io.BufferedReader;
import java.io.IOException;

public class RemoveImage implements Command {
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        System.out.println("Enter the name of the image you want to remove:");
        String name = reader.readLine();
        repo.remove(name);
        System.out.println("Image removed.");
    }
}

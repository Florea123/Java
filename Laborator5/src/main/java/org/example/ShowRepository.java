package org.example;

import java.io.BufferedReader;
import java.io.IOException;

public class ShowRepository implements Command{
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        System.out.println("Images in the repository:");
        repo.getImages().forEach(image -> {
            System.out.println("Name: " + image.name());
            System.out.println("Path: " + image.filePath());
        });
    }
}

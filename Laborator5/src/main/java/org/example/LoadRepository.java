package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class LoadRepository implements Command {
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        System.out.println("Enter the file path to load the repository from:");
        String filePath = reader.readLine();
        File file = new File(filePath);

        if (file.exists() && file.canRead()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Repository loadedRepo = objectMapper.readValue(file, Repository.class);
                repo.setImages(loadedRepo.getImages());
                System.out.println("Repository loaded from " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot read from the specified file path. Please check the permissions.");
        }
    }
}

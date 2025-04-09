package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class SaveRepository implements Command {
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        System.out.println("Enter the file path to save the repository to:");
        String filePath = reader.readLine();
        File file = new File(filePath);

        if (file.canWrite()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(file, repo);
                System.out.println("Repository saved in " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot write to the specified file path. Please check the permissions.");
        }
    }
}
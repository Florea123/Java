package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public interface Command {
    void execute(BufferedReader reader, Repository repo) throws IOException;
}

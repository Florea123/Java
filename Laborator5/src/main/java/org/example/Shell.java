package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Shell {
    private Repository repo;
    private Map<String, Command> commands;

    public Shell() {
        repo=new Repository();
        commands = new HashMap<>();
        commands.put("add", new AddImage());
        commands.put("remove", new RemoveImage());
        commands.put("update", new UpdateImage());
        commands.put("load", new LoadRepository());
        commands.put("save", new SaveRepository());
        commands.put("show", new ShowRepository());
        commands.put("report", new ReportRepository());
    }

    public void run()throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Available commands:add,remove,update,load,save,report");
        while (true) {
            System.out.print("Introduce a command: ");
            String command = reader.readLine();
            Command cmd = commands.get(command);
            if (cmd != null) {
                cmd.execute(reader, repo);
            } else {
                System.out.println("Invalid command");
            }
        }
    }
}

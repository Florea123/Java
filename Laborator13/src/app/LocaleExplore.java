package app;

import com.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    private static final Map<String, Command> commands = new HashMap<>();
    private static final LocaleManager localeManager = new LocaleManager();

    static {
        commands.put("display", new DisplayLocales());
        commands.put("set", new SetLocale());
        commands.put("info", new Info());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            ResourceBundle messages = ResourceBundle.getBundle("res.Messages", localeManager.getCurrentLocale());
            System.out.print(messages.getString("prompt") + " ");
            String line = scanner.nextLine();
            if (line == null || line.equalsIgnoreCase("exit")) break;
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length == 0) continue;
            Command cmd = commands.get(tokens[0].toLowerCase());
            if (cmd == null) {
                System.out.println(messages.getString("invalid"));
            } else {
                cmd.execute(tokens, localeManager, messages);
            }
        }
        scanner.close();
    }
}
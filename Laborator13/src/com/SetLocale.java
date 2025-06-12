package com;

import java.util.Locale;
import java.util.ResourceBundle;

public class SetLocale implements Command {
    @Override
    public void execute(String[] args, LocaleManager localeManager, ResourceBundle messages) {
        if (args.length < 2) {
            System.out.println(messages.getString("invalid"));
            return;
        }
        Locale locale = Locale.forLanguageTag(args[1]);
        localeManager.setCurrentLocale(locale);
        System.out.println(messages.getString("locale.set").replace("{0}", locale.getDisplayName(locale)));
    }
}
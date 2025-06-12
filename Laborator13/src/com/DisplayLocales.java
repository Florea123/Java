package com;

import java.util.Locale;
import java.util.ResourceBundle;

public class DisplayLocales implements Command {
    @Override
    public void execute(String[] args, LocaleManager localeManager, ResourceBundle messages) {
        System.out.println(messages.getString("locales"));
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            System.out.println(locale.toLanguageTag() + " : " + locale.getDisplayName(localeManager.getCurrentLocale()));
        }
    }
}
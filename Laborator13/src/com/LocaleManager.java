package com;

import java.util.Locale;

public class LocaleManager {
    private Locale currentLocale = Locale.getDefault();

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
    }
}
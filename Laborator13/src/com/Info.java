package com;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Info implements Command {
    @Override
    public void execute(String[] args, LocaleManager localeManager, ResourceBundle messages) {
        Locale locale = localeManager.getCurrentLocale();
        if (args.length > 1) {
            locale = Locale.forLanguageTag(args[1]);
        }
        ResourceBundle targetMessages = ResourceBundle.getBundle("res.Messages", locale);

        System.out.println(messages.getString("info").replace("{0}", locale.getDisplayName(locale)));

        System.out.println(targetMessages.getString("country") + ": " +
                locale.getDisplayCountry(locale) + " (" + locale.getDisplayCountry() + ")");
        System.out.println(targetMessages.getString("language") + ": " +
                locale.getDisplayLanguage(locale) + " (" + locale.getDisplayLanguage() + ")");
        try {
            Currency currency = Currency.getInstance(locale);
            System.out.println(targetMessages.getString("currency") + ": " +
                    currency.getCurrencyCode() + " (" + currency.getDisplayName(locale) + ")");
        } catch (Exception e) {
            // Some locales may not have a currency.
        }
        DateFormatSymbols symbols = new DateFormatSymbols(locale);

        System.out.print(targetMessages.getString("weekdays") + ": ");
        String[] weekdays = symbols.getWeekdays();
        for (int i = 1; i < weekdays.length; i++) {
            System.out.print(weekdays[i]);
            if (i < weekdays.length - 1) System.out.print(", ");
        }
        System.out.println();

        System.out.print(targetMessages.getString("months") + ": ");
        String[] months = symbols.getMonths();
        for (int i = 0; i < months.length - 1; i++) {
            System.out.print(months[i]);
            if (i < months.length - 2) System.out.print(", ");
        }
        System.out.println();

        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
        System.out.println(targetMessages.getString("today") + ": " + df.format(now));
    }
}
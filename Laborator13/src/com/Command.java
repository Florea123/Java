package com;

import java.util.ResourceBundle;

public interface Command {
    void execute(String[] args, LocaleManager localeManager, ResourceBundle messages);
}
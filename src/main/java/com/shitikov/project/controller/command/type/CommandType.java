package com.shitikov.project.controller.command.type;

import com.shitikov.project.controller.command.Command;
import com.shitikov.project.controller.command.impl.*;
import com.shitikov.project.controller.command.impl.open.page.AccountPageCommand;
import com.shitikov.project.controller.command.impl.open.page.HomePageCommand;
import com.shitikov.project.controller.command.impl.open.page.LoginPageCommand;
import com.shitikov.project.controller.command.impl.open.page.RegistrationPageCommand;

public enum CommandType {
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    REGISTRATION(new RegistrationCommand()),
    EMPTY_COMMAND(new EmptyCommand()),
    CHANGE_LANGUAGE(new ChangeLanguageCommand()),
    ACTIVATE_ACCOUNT(new ActivateAccountCommand()),
    //pages
    HOME_PAGE(new HomePageCommand()),
    REGISTRATION_PAGE(new RegistrationPageCommand()),
    LOGIN_PAGE(new LoginPageCommand()),
    ACCOUNT_PAGE(new AccountPageCommand());


    private Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}

package com.alexander.orshadiarybot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "message")
public class MessageProperty {
    private String chooseActionMessage = "Choose action";

    private String accountListMessage = "Your added accounts: %s";

    private String enterPhoneMessage = "Enter phone";

    private String enterPasswordMessage = "Enter password";

    private String wrongCredentialsMessage = "Wrong phone or password";

    private String noLinkedAccountsMessage = "You have no linked accounts";

    private String chooseAccountUserMessage = "Choose account";

    private String wrongNameMessage = "Wrong name";

    private String chooseSubjectMessage = "Choose subject";

    private String marksBySubjectMessage = "Marks by subject %s: %s";

    private String marksMessage = "Marks %s:%s";

    private String takeCookiesErrorMessage = "Some error while trying to log-in as %s";

    private String accountWasAddedMessage = "Account was successfully added";

    private String addAccountAction = "Add account";

    private String listAccountsAction = "List accounts";

    private String showMarksAction = "Show marks";

    private String unknownCommandMessage = "Unknown command";

    private String newMarksMessage = "%s have new marks:\n%s";

    private String removedMarksMessage = "%s have removed marks:\n%s";

    private String allMarksOption = "All";
}

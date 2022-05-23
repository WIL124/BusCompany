package thumbtack.buscompany.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    LOGIN_ALREADY_EXISTS("This login has already been registered"),
    WRONG_PASSWORD("Wrong password, try again"),
    ONE_ACTIVE_ADMIN("At least one admin must be online"),
    USER_NOT_FOUND("User not found"),
    DO_NOT_HAVE_PERMISSIONS("Don't have permissions"),
    NOT_AN_ADMIN("The request can only be executed by an administrator"),
    NOT_A_CLIENT("The request can only be executed by an administrator"),
    SESSION_NOT_FOUND("Session not found"),
    DIFFERENT_PASSWORDS("passwords must match");
    private final String message;
}

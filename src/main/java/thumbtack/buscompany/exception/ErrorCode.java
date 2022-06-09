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
    DIFFERENT_PASSWORDS("passwords must match"),
    NOT_FOUND("not found"),
    INVALID_PLACE("Please, choice any free place"),
    WRONG_DATE_INTERVAL("the specified interval and period does not contain a single day"),
    TRIP_NOT_FOUND("trip not found"),
    ORDER_NOT_FOUND("order not found");
    private final String message;
}

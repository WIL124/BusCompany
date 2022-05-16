package thumbtack.buscompany.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    LOGIN_ALREADY_EXISTS("This login has already been registered"),
    WRONG_PASSWORD("Wrong password, try again"),
    DELETED_USER("User deleted"),
    ONE_ACTIVE_ADMIN("At least one admin must be online"),
    SESSION_NOT_FOUND("Session not found");
    private final String message;
}

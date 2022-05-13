package thumbtack.buscompany.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerException extends Exception {
    private ErrorCode errorCode;
    private String field;
}

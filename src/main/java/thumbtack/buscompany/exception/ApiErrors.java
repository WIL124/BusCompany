package thumbtack.buscompany.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrors {
    private String errorCode;
    private String field;
    private String message;

    public ApiErrors(ErrorCode errorCode, String field) {
        this.errorCode = errorCode.name();
        this.field = field;
        this.message = errorCode.getMessage();
    }
}

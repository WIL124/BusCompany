package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrors {
    private String errorCode;
    private String field;
    private String message;
}

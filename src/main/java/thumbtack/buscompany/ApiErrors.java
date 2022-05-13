package thumbtack.buscompany;

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
}

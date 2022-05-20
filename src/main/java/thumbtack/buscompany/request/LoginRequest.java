package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import thumbtack.buscompany.validators.MaxSize;
import thumbtack.buscompany.validators.MinPassLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "null login")
    String login;
    @NotBlank
    String password;
}

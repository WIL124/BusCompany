package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validators.Fio;
import thumbtack.buscompany.validators.MaxSize;
import thumbtack.buscompany.validators.MinPassLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {
    @Fio
    @MaxSize
    @NotBlank
    private String firstName;
    @Fio
    @MaxSize
    @NotBlank
    private String lastName;
    @Fio
    @MaxSize
    private String patronymic;
    private String position;
    @NotBlank(message = "null login")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\d]+$")
    @MaxSize
    private String login;
    @NotBlank
    @MaxSize
    @MinPassLength
    private String password;
}

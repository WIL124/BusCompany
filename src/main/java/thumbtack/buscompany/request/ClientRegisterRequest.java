package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validators.Fio;
import thumbtack.buscompany.validators.MaxSize;
import thumbtack.buscompany.validators.MinPassLength;
import thumbtack.buscompany.validators.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRegisterRequest {
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
    @Email
    private String email;
    @Phone
    private String phone;
    @NotBlank(message = "null login")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\d]+$")
    @MaxSize
    private String login;
    @NotBlank
    @MaxSize
    @MinPassLength
    private String password;
}

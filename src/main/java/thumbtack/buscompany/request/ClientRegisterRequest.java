package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validator.annototion.ValidName;
import thumbtack.buscompany.validator.annototion.MaxSize;
import thumbtack.buscompany.validator.annototion.MinPassLength;
import thumbtack.buscompany.validator.annototion.Phone;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRegisterRequest {
    @ValidName
    @MaxSize
    @NotBlank
    private String firstName;
    @ValidName
    @MaxSize
    @NotBlank
    private String lastName;
    @ValidName
    @MaxSize
    private String patronymic;
    @Email
    private String email;
    @Phone
    private String phone;
    @NotBlank
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\d]+$")
    @MaxSize
    private String login;
    @NotBlank
    @MaxSize
    @MinPassLength
    private String password;
}

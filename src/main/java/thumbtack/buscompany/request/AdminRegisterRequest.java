package thumbtack.buscompany.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validator.annototion.ValidName;
import thumbtack.buscompany.validator.annototion.MaxSize;
import thumbtack.buscompany.validator.annototion.MinPassLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {
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
    private String position;
    @NotBlank
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\d]+$")
    @MaxSize
    private String login;
    @NotBlank
    @MaxSize
    @MinPassLength
    private String password;
}

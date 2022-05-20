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
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateRequest {
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
    @NotBlank
    @MaxSize
    @MinPassLength
    private String newPassword;
    @NotNull
    private String oldPassword;
}

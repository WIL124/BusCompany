package thumbtack.buscompany.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.validator.annototion.ValidName;
import thumbtack.buscompany.validator.annototion.MaxSize;
import thumbtack.buscompany.validator.annototion.MinPassLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateRequest {
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
    @MaxSize
    @MinPassLength
    private String newPassword;
    @NotNull
    private String oldPassword;
}

package thumbtack.buscompany.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.model.UserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private UserType userType;
}

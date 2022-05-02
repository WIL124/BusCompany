package thumbtack.buscompany.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thumbtack.buscompany.model.UserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateResponse {
    private String firstname;
    private String lastname;
    private String patronymic;
    private String position;
    private UserType userType;
}

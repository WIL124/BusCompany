package thumbtack.buscompany.response;

import lombok.Data;
import thumbtack.buscompany.model.UserType;

@Data
public abstract class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private UserType userType;
}

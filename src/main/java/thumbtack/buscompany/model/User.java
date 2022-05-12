package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private UserType userType;

    public void loginToLowerCase() {
        login = login.toLowerCase();
    }
}

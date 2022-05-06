package thumbtack.buscompany.model;

import lombok.Data;

@Data
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

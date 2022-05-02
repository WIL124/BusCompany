package thumbtack.buscompany.model;

import lombok.Data;

@Data
public abstract class User {
    private Integer id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String login;
    private String password;
}

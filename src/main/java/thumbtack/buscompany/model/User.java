package thumbtack.buscompany.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    // REVU просто int. Integer - если имеет смысл значение null. Тут не имеет
    // здесь и везде
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private UserType userType;
}

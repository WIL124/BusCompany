package thumbtack.buscompany.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;
    private UserType userType;
}

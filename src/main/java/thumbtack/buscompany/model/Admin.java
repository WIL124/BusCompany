package thumbtack.buscompany.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    private String position;

public Admin(Integer id, String firstName, String lastName, String patronymic, String login, String password, UserType userType, String position) {
        super(id, firstName, lastName, patronymic, login, password, userType, true);
        this.position = position;
    }
}

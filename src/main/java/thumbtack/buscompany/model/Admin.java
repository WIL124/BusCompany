package thumbtack.buscompany.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    private String position;

    public Admin(){
        this.setUserType(UserType.ADMIN);
    }
public Admin(Integer id, String firstName, String lastName, String patronymic, String login, String password, String position) {
        super(id, firstName, lastName, patronymic, login, password, UserType.ADMIN);
        this.position = position;
    }
}

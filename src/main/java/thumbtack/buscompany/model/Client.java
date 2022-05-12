package thumbtack.buscompany.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends User {
    private String email;
    private String phone;

    public Client(Integer id, String firstName, String lastName, String patronymic, String login, String password, UserType userType, String email, String phone) {
        super(id, firstName, lastName, patronymic, login, password, userType);
        this.email = email;
        this.phone = phone;
    }
}

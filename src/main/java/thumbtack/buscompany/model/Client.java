package thumbtack.buscompany.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends User {
    private String email;
    private String phone;
    private List<Order> orders;

    public Client() {
        this.setUserType(UserType.CLIENT);
    }

    public Client(Integer id, String firstName, String lastName, String patronymic, String login, String password, String email, String phone) {
        super(id, firstName, lastName, patronymic, login, password, UserType.CLIENT);
        this.email = email;
        this.phone = phone;
        this.orders = new ArrayList<>();
    }

    public void canonizePhoneFormat() {
        phone = phone.replaceAll("-", "");
    }
}

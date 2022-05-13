package thumbtack.buscompany;

import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;

public class TestUtils {
    public static User createUser() {
        return new User(null, "Владислав", "Инютин", "Игоревич",
                "userLogin", "goodPassword", UserType.ADMIN, true);
    }

    public static Client createClient() {
        return new Client(null, "Владислав", "Инютин", "Игоревич",
                "clientLogin", "goodPassword", UserType.ADMIN,
                "vladislav@mail.ru", "89087961203");
    }

    public static Admin createAdmin() {
        return new Admin(null, "Владислав", "Инютин", "Игоревич",
                "adminLogin", "goodPassword", UserType.ADMIN,
                "leader");
    }
    public static AdminRegisterRequest createAdminRegReq(){
        return new AdminRegisterRequest("Владислав", "Инютин",
                null, "admin", "adminLogin", "goodPassword");
    }
    public static ClientRegisterRequest createClientRegReq(){
        return new ClientRegisterRequest("Клиент", "Фамилия",
                null, "client@mail.ru", "8-908-796-12-44", "clientLogin", "goodPassword");
    }
}

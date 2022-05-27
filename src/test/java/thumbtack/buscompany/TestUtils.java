package thumbtack.buscompany;

import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.request.*;

import java.util.List;

public class TestUtils {

    public static Client createClient() {
        return new Client(null, "Владислав", "Инютин", "Игоревич",
                "clientLogin", "goodPassword",
                "vladislav@mail.ru", "89087961203");
    }

    public static Admin createAdmin() {
        return new Admin(null, "Владислав", "Инютин", "Игоревич",
                "adminLogin", "goodPassword",
                "leader");
    }

    public static AdminRegisterRequest createAdminRegReq() {
        return new AdminRegisterRequest("Владислав", "Инютин",
                null, "admin", "adminLogin", "goodPassword");
    }

    public static ClientRegisterRequest createClientRegReq() {
        return new ClientRegisterRequest("Клиент", "Фамилия",
                null, "client@mail.ru", "8-908-796-12-44", "clientLogin", "goodPassword");
    }

    public static AdminUpdateRequest createAdminUpdateReq() {
        return new AdminUpdateRequest("Владислав", "Инютин", "Игоревич", "admin", "goodNewPass", "goodOldPass");
    }

    public static TripRequest createTripRequest() {
        return new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null);
    }

    public static ScheduleDto createScheduleDto() {
        return new ScheduleDto("2020.12.01", "2020.12.30", "daily");
    }
}

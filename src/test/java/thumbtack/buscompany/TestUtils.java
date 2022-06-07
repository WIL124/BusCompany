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

    public static TripRequest createTripRequestWithSchedule() {
        return new TripRequest("VOLVO", "Omsk", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null);
    }
    public static TripRequest createTripRequestWithDates() {
        return new TripRequest("VOLVO", "Omsk", "Moscow", "04:30", "12:00", 100L, null, createDates());
    }

    public static ScheduleDto createScheduleDto() {
        return new ScheduleDto("2020.12.01", "2020.12.30", "daily");
    }

    public static List<String> createDates() {
        return List.of("2022.12.01", "2021.01.05", "2022.05.04", "2022.05.05");
    }
}

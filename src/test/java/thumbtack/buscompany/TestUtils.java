package thumbtack.buscompany;

import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class TestUtils {

    public static Client createClient() {
        return new Client(0, "Владислав", "Инютин", "Игоревич",
                "clientLogin", "goodPassword",
                "vladislav@mail.ru", "89087961203");
    }

    public static Admin createAdmin() {
        return new Admin(0, "Владислав", "Инютин", "Игоревич",
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

    public static OrderRequest createOrderRequest(Integer tripId, LocalDate date) {
        return new OrderRequest(tripId, date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), createPassengersList());
    }

    public static List<Passenger> createPassengersList() {
        return List.of(new Passenger("Vlad", "Inyutin", 123),
                new Passenger("Misha", "Popov", 321),
                new Passenger("Denis", "Den", 231));
    }
    public static OrderRequest createOrderRequestWith15Passengers(Integer tripId, LocalDate date) {
        return new OrderRequest(tripId, date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), fifteenPassengersList());
    }
    private static List<Passenger> fifteenPassengersList() {
        return List.of(new Passenger("Vlad", "Inyutin", 123),
                new Passenger("Misha", "Popov", 321),
                new Passenger("Denis", "Den", 231),
                new Passenger("Vlad", "Inyutin", 123),
                new Passenger("Misha", "Popov", 321),
                new Passenger("Denis", "Den", 231),
                new Passenger("Vlad", "Inyutin", 123),
                new Passenger("Misha", "Popov", 321),
                new Passenger("Denis", "Den", 231),
                new Passenger("Vlad", "Inyutin", 123),
                new Passenger("Misha", "Popov", 321),
                new Passenger("Denis", "Den", 231),
                new Passenger("Vlad", "Inyutin", 123),
                new Passenger("Misha", "Popov", 321),
                new Passenger("Denis", "Den", 231));
    }
    public static ChoosingPlaceRequest createChoosingPlaceRequest(Integer orderId, Passenger passenger, Integer place){
        return new ChoosingPlaceRequest(orderId,  passenger.getLastName(),passenger.getFirstName(), passenger.getPassport(), place);
    }
}

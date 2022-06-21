package thumbtack.buscompany.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import thumbtack.buscompany.BuscompanyApplicationTests;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.TripResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static thumbtack.buscompany.TestUtils.createTripRequestWithSchedule;

public class TripServiceTest extends BuscompanyApplicationTests {
    @Autowired
    TripService tripService;
    @MockBean
    TripDao tripDao;
    @MockBean
    BusService busService;
    @MockBean
    SessionDao sessionDao;

    @BeforeEach
    void setUp() throws ServerException {
        when(busService.get(anyString())).thenReturn(new Bus("Volvo", 10));
        when(sessionDao.getSessionById(anyString())).thenReturn(Optional.of(new Session(new Admin())));
    }

    @Test
    public void tripServiceGenerateDatesFromScheduleWithDailyPeriod() throws ServerException {
        TripRequest request = createTripRequestWithSchedule();
        request.getScheduleDto().setFromDate("2000.01.01");
        request.getScheduleDto().setToDate("2000.01.10");
        TripResponse tripResponse = tripService.create(request, anyString());
        assertEquals(10, tripResponse.getDates().size());
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isAfter(LocalDate.of(1999, 12, 31))));
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isBefore(LocalDate.of(2000, 1, 11))));
    }

    @Test
    public void tripServiceGenerateDatesFromScheduleWithEvenPeriod() throws ServerException {
        TripRequest request = createTripRequestWithSchedule();
        request.getScheduleDto().setFromDate("2000.01.01");
        request.getScheduleDto().setToDate("2000.01.10");
        request.getScheduleDto().setPeriod("even");
        TripResponse tripResponse = tripService.create(request, anyString());
        assertEquals(5, tripResponse.getDates().size());
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isAfter(LocalDate.of(1999, 12, 31))));
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isBefore(LocalDate.of(2000, 1, 11))));
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.getDayOfMonth() % 2 == 0));
    }

    @Test
    public void tripServiceGenerateDatesFromScheduleWithOddPeriod() throws ServerException {
        TripRequest request = createTripRequestWithSchedule();
        request.getScheduleDto().setFromDate("2000.01.01");
        request.getScheduleDto().setToDate("2000.01.10");
        request.getScheduleDto().setPeriod("odd");
        TripResponse tripResponse = tripService.create(request, anyString());
        assertEquals(5, tripResponse.getDates().size());
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isAfter(LocalDate.of(1999, 12, 31))));
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isBefore(LocalDate.of(2000, 1, 11))));
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.getDayOfMonth() % 2 != 0));
    }

    @Test
    public void tripServiceGenerateDatesFromScheduleWithDayNumbersPeriod() throws ServerException {
        TripRequest request = createTripRequestWithSchedule();
        request.getScheduleDto().setFromDate("2000.01.01");
        request.getScheduleDto().setToDate("2000.01.10");
        request.getScheduleDto().setPeriod("1,2,5,9");
        List<Integer> nums = List.of(1, 2, 5, 9);
        TripResponse tripResponse = tripService.create(request, anyString());
        assertEquals(4, tripResponse.getDates().size());
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isAfter(LocalDate.of(1999, 12, 31))));
        assertTrue(tripResponse.getDates().stream().allMatch(e -> e.isBefore(LocalDate.of(2000, 1, 11))));
        assertTrue(nums.containsAll(tripResponse.getDates().stream().map(LocalDate::getDayOfMonth).collect(Collectors.toList())));
    }
}

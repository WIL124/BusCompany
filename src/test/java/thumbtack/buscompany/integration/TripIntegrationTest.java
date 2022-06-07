package thumbtack.buscompany.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import thumbtack.buscompany.BuscompanyApplicationTests;
import thumbtack.buscompany.exception.ApiErrors;
import thumbtack.buscompany.exception.Errors;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.TripMapper;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.ClientResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;
import static thumbtack.buscompany.TestUtils.*;
import static thumbtack.buscompany.exception.ErrorCode.TRIP_NOT_FOUND;

public class TripIntegrationTest extends BuscompanyApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TripMapper tripMapper;
    private static final String TRIP_URL = "/api/trips";
    private String adminSessionId = null;
    private String clientSessionId = null;

    @BeforeEach
    public void clear() {
        restTemplate.postForEntity("/api/debug/clear", null, Void.class);
    }

    @Test
    public void adminInsertAndGetTrip() throws ServerException {
        String session = registerAdminAndGetSessionId("adminLogin");
        TripRequest tripRequest = createTripRequestWithDates();
        HttpEntity<Object> entity = entityWithSessionId(tripRequest, session);
        ResponseEntity<Trip> insertTripResponseEntity = restTemplate.exchange(TRIP_URL, HttpMethod.POST, entity, Trip.class);
        Trip insertedTrip = tripMapper.tripFromRequest(tripRequest);
        Trip tripFromResponse = insertTripResponseEntity.getBody();
        assert tripFromResponse != null;
        assertAll(
                () -> assertEquals(200, insertTripResponseEntity.getStatusCodeValue()),
                () -> {
                    insertedTrip.setTripId(tripFromResponse.getTripId());
                    assertEquals(insertedTrip, insertTripResponseEntity.getBody());
                }
        );

        ResponseEntity<Trip> getTripResponseEntity = restTemplate.exchange(TRIP_URL + "/" + tripFromResponse.getTripId().toString(),
                HttpMethod.GET, entityWithSessionId(null, session), Trip.class);
        assertAll(
                () -> assertEquals(200, getTripResponseEntity.getStatusCodeValue()),
                () -> assertTrue(isEqualCollection(tripFromResponse.getDates(), getTripResponseEntity.getBody().getDates()))
        );
    }

    @Test
    public void updateAndGetTrip() throws ServerException {
        ResponseEntity<Trip> response = insertTrip(createTripRequestWithDates());
        assert response.getBody() != null;
        TripRequest tripRequest = createTripRequestWithSchedule();
        tripRequest.setPrice(1234L);
        tripRequest.setStart("11:45");
        tripRequest.setFromStation("Калининград");
        HttpEntity<Object> entity = entityWithSessionId(tripRequest, adminSessionId);
        ResponseEntity<Trip> updateResponse = restTemplate.exchange(TRIP_URL + "/" + response.getBody().getTripId(), HttpMethod.PUT, entity, Trip.class);
        Trip tripFromRequest = tripMapper.tripFromRequest(tripRequest);
        Trip tripFromUpdate = updateResponse.getBody();
        assert tripFromUpdate != null;
        assertAll(
                () -> assertEquals(200, updateResponse.getStatusCodeValue()),
                () -> assertEquals(tripFromRequest.getFromStation(), tripFromUpdate.getFromStation()),
                () -> assertEquals(tripFromRequest.getSchedule(), tripFromUpdate.getSchedule()),
                () -> assertEquals(tripFromRequest.getBus(), tripFromUpdate.getBus()),
                () -> assertEquals(tripFromRequest.getPrice(), tripFromUpdate.getPrice()),
                () -> assertEquals(30, tripFromUpdate.getDates().size())
        );

        Trip tripFromGet = restTemplate.exchange(TRIP_URL + "/" + tripFromUpdate.getTripId().toString(),
                HttpMethod.GET, entityWithSessionId(null, adminSessionId), Trip.class).getBody();
        tripFromUpdate.setSchedule(null);
        assertEquals(tripFromUpdate, tripFromGet);
    }

    @Test
    public void deleteAndGetTrip() {
        Trip trip = insertTrip(createTripRequestWithDates()).getBody();
        ResponseEntity<Void> deleteEntity = restTemplate.exchange(TRIP_URL + "/" + trip.getTripId(), HttpMethod.DELETE,
                entityWithSessionId(null, adminSessionId), Void.class);
        assertEquals(200, deleteEntity.getStatusCodeValue());
        ResponseEntity<Errors> getEntity = restTemplate.exchange(TRIP_URL + "/" + trip.getTripId(), HttpMethod.GET,
                entityWithSessionId(null, adminSessionId), Errors.class);
        assertAll(
                () -> assertEquals(400, getEntity.getStatusCodeValue()),
                () -> assertEquals(1, getEntity.getBody().getErrors().size()),
                () -> assertEquals(new ApiErrors(TRIP_NOT_FOUND, "tripId"),
                        getEntity.getBody().getErrors().get(0))
        );
    }

    @Test
    public void getTripsWithoutParams() {
        List<Trip> trips = createListOfTrips();
        trips.forEach(trip -> trip.setSchedule(null));
        ResponseEntity<List<Trip>> responseEntity =
                restTemplate.exchange(TRIP_URL, HttpMethod.GET, entityWithSessionId(null, adminSessionId),
                        new ParameterizedTypeReference<>() {
                        });
        assert responseEntity.getBody() != null;
        List<Trip> tripFromResponse = responseEntity.getBody();
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(6, tripFromResponse.size())
        );
        for (int i = 0; i < 6; i++) {
            assertTrue(isEqualCollection(trips.get(i).getDates(),
                    tripFromResponse.get(i).getDates()));
        }

    }

    @Test
    public void getTripsWithAllParams() {
        List<Trip> trips = createListOfTrips();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String fromStation = "Omsk";
        String toStation = "Moscow";
        String busName = "VOLVO";
        LocalDate fromDate = LocalDate.of(2020, 12, 1);
        LocalDate toDate = LocalDate.of(2020, 12, 15);
        String url = TRIP_URL +
                "?fromStation=" + fromStation +
                "&toStation=" + toStation +
                "&busName=" + busName +
                "&fromDate=" + fromDate.format(formatter) +
                "&toDate=" + toDate.format(formatter);
        ResponseEntity<List<Trip>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entityWithSessionId(null, adminSessionId), new ParameterizedTypeReference<List<Trip>>() {
                });

        assert responseEntity.getBody() != null;
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getBus().getBusName().equals(busName))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getFromStation().equals(fromStation))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getToStation().equals(toStation))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().stream().allMatch(date -> date.isAfter(fromDate)))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().stream().allMatch(date -> date.isBefore(toDate))))
        );
    }

    private List<Trip> createListOfTrips() {
        List<TripRequest> list = List.of(
                new TripRequest("VOLVO", "Omsk", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null),
                new TripRequest("PAZ", "Saratov", "Omsk", "13:25", "01:25", 151L, createScheduleDto(), null),
                new TripRequest("YAZ", "Moscow", "Omsk", "02:30", "16:55", 200L, createScheduleDto(), null),
                new TripRequest("VOLVO", "Omsk", "Moscow", "05:30", "12:35", 300L, null, createDates()),
                new TripRequest("PAZ", "Saratov", "Omsk", "13:25", "01:25", 151L, null, createDates()),
                new TripRequest("YAZ", "Moscow", "Omsk", "12:30", "16:55", 450L, null, createDates()));
        String session = registerAdminAndGetSessionId("testAdmin");
        List<Trip> result = new ArrayList<>();
        for (TripRequest tr : list) {
            result.add(restTemplate.exchange(TRIP_URL, HttpMethod.POST, entityWithSessionId(tr, session), Trip.class).getBody());
        }
        return result;
    }

    private ResponseEntity<Trip> insertTrip(TripRequest tripRequest) {
        String session = registerAdminAndGetSessionId("testAdmin");
        HttpEntity<Object> entity = entityWithSessionId(tripRequest, session);
        return restTemplate.exchange(TRIP_URL, HttpMethod.POST, entity, Trip.class);
    }

    private String getSessionId(ResponseEntity<?> response) {
        return Objects.requireNonNull(response.getHeaders().get(HttpHeaders.SET_COOKIE)).get(0);
    }

    private String registerAdminAndGetSessionId(String login) {
        AdminRegisterRequest adminRegReq = createAdminRegReq();
        adminRegReq.setLogin(login);
        ResponseEntity<AdminResponse> response = restTemplate
                .postForEntity("/api/admins", adminRegReq, AdminResponse.class);
        adminSessionId = getSessionId(response);
        return adminSessionId;
    }

    private String registerClientAndGetSessionId(String login) {
        ClientRegisterRequest clientRegReq = createClientRegReq();
        clientRegReq.setLogin(login);
        ResponseEntity<ClientResponse> response = restTemplate
                .postForEntity("/api/clients", clientRegReq, ClientResponse.class);
        clientSessionId = getSessionId(response);
        return clientSessionId;
    }

    private HttpEntity<Object> entityWithSessionId(Object body, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionId);
        return new HttpEntity<>(body, headers);
    }
}

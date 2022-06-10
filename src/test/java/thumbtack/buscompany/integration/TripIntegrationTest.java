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
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.ClientResponse;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.response.TripResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private static final String ORDER_URL = "/api/orders";
    private String adminSessionId = null;
    private String clientSessionId = null;

    @BeforeEach
    public void clear() {
        restTemplate.postForEntity("/api/debug/clear", null, Void.class);
    }

    @Test
    public void adminInsertAndGetTrip() {
        String session = registerAdminAndGetSessionId("adminLogin");
        TripRequest tripRequest = createTripRequestWithDates();
        HttpEntity<Object> entity = entityWithSessionId(tripRequest, session);
        ResponseEntity<TripResponse> responseEntity = restTemplate.exchange(TRIP_URL, HttpMethod.POST, entity, TripResponse.class);
        TripResponse tripResponse = responseEntity.getBody();
        assert tripResponse != null;
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(tripResponse.getTripId()),
                () -> assertEquals(tripRequest.getDates(),
                        tripResponse.getDates().stream().map(date -> date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))).collect(Collectors.toList()))
        );

        ResponseEntity<TripResponse> getTripResponseEntity = restTemplate.exchange(TRIP_URL + "/" + tripResponse.getTripId().toString(),
                HttpMethod.GET, entityWithSessionId(null, session), TripResponse.class);
        assertAll(
                () -> assertEquals(200, getTripResponseEntity.getStatusCodeValue())
        );
    }

    @Test
    public void updateAndGetTrip() throws ServerException {
        ResponseEntity<TripResponse> response = insertTrip(createTripRequestWithDates());
        assert response.getBody() != null;
        TripRequest tripRequest = createTripRequestWithSchedule();
        tripRequest.setPrice(1234L);
        tripRequest.setStart("11:45");
        tripRequest.setFromStation("Калининград");
        HttpEntity<Object> entity = entityWithSessionId(tripRequest, adminSessionId);
        ResponseEntity<TripResponse> updateResponse = restTemplate.exchange(TRIP_URL + "/" + response.getBody().getTripId(), HttpMethod.PUT, entity, TripResponse.class);
        Trip tripFromRequest = tripMapper.tripFromRequest(tripRequest);
        TripResponse tripFromUpdate = updateResponse.getBody();
        assert tripFromUpdate != null;
        assertAll(
                () -> assertEquals(200, updateResponse.getStatusCodeValue()),
                () -> assertEquals(tripFromRequest.getFromStation(), tripFromUpdate.getFromStation()),
                () -> assertEquals(tripFromRequest.getSchedule(), tripFromUpdate.getSchedule()),
                () -> assertEquals(tripFromRequest.getBus(), tripFromUpdate.getBus()),
                () -> assertEquals(tripFromRequest.getPrice(), tripFromUpdate.getPrice()),
                () -> assertEquals(30, tripFromUpdate.getDates().size())
        );

        TripResponse tripFromGet = restTemplate.exchange(TRIP_URL + "/" + tripFromUpdate.getTripId().toString(),
                HttpMethod.GET, entityWithSessionId(null, adminSessionId), TripResponse.class).getBody();
        tripFromUpdate.setSchedule(null);
        assertEquals(tripFromUpdate, tripFromGet);
    }

    @Test
    public void deleteAndGetTrip() {
        TripResponse tripResponse = insertTrip(createTripRequestWithDates()).getBody();
        ResponseEntity<Void> deleteEntity = restTemplate.exchange(TRIP_URL + "/" + tripResponse.getTripId(), HttpMethod.DELETE,
                entityWithSessionId(null, adminSessionId), Void.class);
        assertEquals(200, deleteEntity.getStatusCodeValue());
        ResponseEntity<Errors> getEntity = restTemplate.exchange(TRIP_URL + "/" + tripResponse.getTripId(), HttpMethod.GET,
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
        List<TripResponse> trips = createListOfTrips();
        trips.forEach(trip -> trip.setSchedule(null));
        ResponseEntity<List<TripResponse>> responseEntity =
                restTemplate.exchange(TRIP_URL, HttpMethod.GET, entityWithSessionId(null, adminSessionId),
                        new ParameterizedTypeReference<List<TripResponse>>() {
                        });
        assert responseEntity.getBody() != null;
        List<TripResponse> tripFromResponse = responseEntity.getBody();
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
    public void getTripsWithAllParamsByAdminAndEmptyBodyForClient() {
        createListOfTrips();
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
        ResponseEntity<List<TripResponse>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entityWithSessionId(null, adminSessionId), new ParameterizedTypeReference<>() {
                });

        assert responseEntity.getBody() != null;
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getBus().getBusName().equals(busName))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getFromStation().equals(fromStation))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getToStation().equals(toStation))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getApproved().equals(false))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().stream().allMatch(date -> date.isAfter(fromDate)))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().stream().allMatch(date -> date.isBefore(toDate))))
        );

        registerClientAndGetSessionId("clientLogin");
        ResponseEntity<List<TripResponse>> emptyEntity =
                restTemplate.exchange(url, HttpMethod.GET, entityWithSessionId(null, clientSessionId), new ParameterizedTypeReference<>() {
                });
        assertAll(
                () -> assertEquals(200, emptyEntity.getStatusCodeValue()),
                () -> assertEquals(0, emptyEntity.getBody().size())
        );
    }

    @Test
    public void getApprovedTripsWithParamsByClient() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String fromStation = "Omsk";
        String toStation = "Moscow";
        String busName = "VOLVO";
        LocalDate fromDate = LocalDate.of(2021, 1, 1);
        LocalDate toDate = LocalDate.of(2022, 1, 1);
        String url = TRIP_URL +
                "?fromStation=" + fromStation +
                "&toStation=" + toStation +
                "&busName=" + busName +
                "&fromDate=" + fromDate.format(formatter) +
                "&toDate=" + toDate.format(formatter);
        registerClientAndGetSessionId("clientLogin");
        TripResponse tripResponse = insertTrip(createTripRequestWithDates()).getBody();
        approveTrip(tripResponse.getTripId());
        ResponseEntity<List<TripResponse>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entityWithSessionId(null, clientSessionId), new ParameterizedTypeReference<>() {
                });
        assert responseEntity.getBody() != null;
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertEquals(1, responseEntity.getBody().size()),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().size() == 1)),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getBus().getBusName().equals(busName))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getFromStation().equals(fromStation))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getToStation().equals(toStation))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getApproved() == null)),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().stream().allMatch(date -> date.isAfter(fromDate)))),
                () -> assertTrue(responseEntity.getBody().stream().allMatch(e -> e.getDates().stream().allMatch(date -> date.isBefore(toDate))))
        );
    }

    @Test
    public void bookingTickets() {
        TripResponse tripResponse = insertTrip(createTripRequestWithDates()).getBody();
        assert tripResponse != null;
        approveTrip(tripResponse.getTripId());
        registerClientAndGetSessionId("clientLogin");
        OrderRequest orderRequest = createOrderRequest(tripResponse.getTripId(), tripResponse.getDates().get(2));
        ResponseEntity<OrderResponse> response = restTemplate.exchange(ORDER_URL, HttpMethod.POST, entityWithSessionId(orderRequest, clientSessionId), OrderResponse.class);
        assertEquals(200, response.getStatusCodeValue());
        OrderResponse orderResponse = response.getBody();
        assertAll(
                () -> assertNotNull(orderResponse.getOrderId()),
                () -> assertEquals(tripResponse.getTripId(), orderResponse.getTripId()),
                () -> assertEquals(tripResponse.getFromStation(), orderResponse.getFromStation()),
                () -> assertEquals(tripResponse.getDuration(), orderResponse.getDuration()),
                () -> assertEquals(tripResponse.getPrice(), orderResponse.getPrice()),
                () -> assertEquals(tripResponse.getPrice() * orderRequest.getPassengers().size(), orderResponse.getTotalPrice())
        );

    }

    private List<TripResponse> createListOfTrips() {
        List<TripRequest> list = List.of(
                new TripRequest("VOLVO", "Omsk", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null),
                new TripRequest("PAZ", "Saratov", "Omsk", "13:25", "01:25", 151L, createScheduleDto(), null),
                new TripRequest("YAZ", "Moscow", "Omsk", "02:30", "16:55", 200L, createScheduleDto(), null),
                new TripRequest("VOLVO", "Omsk", "Moscow", "05:30", "12:35", 300L, null, createDates()),
                new TripRequest("PAZ", "Saratov", "Omsk", "13:25", "01:25", 151L, null, createDates()),
                new TripRequest("YAZ", "Moscow", "Omsk", "12:30", "16:55", 450L, null, createDates()));
        String session = registerAdminAndGetSessionId("testAdmin");
        List<TripResponse> result = new ArrayList<>();
        for (TripRequest tr : list) {
            result.add(restTemplate.exchange(TRIP_URL, HttpMethod.POST, entityWithSessionId(tr, session), TripResponse.class).getBody());
        }
        return result;
    }

    private TripResponse approveTrip(Integer tripId) {
        return restTemplate.exchange(TRIP_URL + "/" + tripId + "/approve", HttpMethod.PUT, entityWithSessionId(null, adminSessionId), TripResponse.class).getBody();
    }

    private ResponseEntity<TripResponse> insertTrip(TripRequest tripRequest) {
        String session = registerAdminAndGetSessionId("testedAdmin");
        HttpEntity<Object> entity = entityWithSessionId(tripRequest, session);
        return restTemplate.exchange(TRIP_URL, HttpMethod.POST, entity, TripResponse.class);
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

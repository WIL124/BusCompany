package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.ParamsMapper;
import thumbtack.buscompany.mapper.TripMapper;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.TripResponse;
import thumbtack.buscompany.service.SessionService;
import thumbtack.buscompany.service.TripService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trips")
public class TripController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    TripService tripService;
    TripMapper tripMapper;
    SessionService sessionService;
    ParamsMapper paramsMapper;

    @PostMapping
    public TripResponse createTrip(@Valid @RequestBody TripRequest tripRequest,
                                   @CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        return tripService.create(tripRequest, sessionId);
    }

    @PutMapping("/{tripId}")
    public TripResponse updateTrip(@Valid @RequestBody TripRequest tripRequest,
                                   @CookieValue(value = JAVASESSIONID) @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        return tripService.update(tripId, tripRequest, sessionId);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId,
                                           @PathVariable("tripId") int tripId) throws ServerException {
        return tripService.deleteTrip(tripId, sessionId);
    }

    @GetMapping("/{tripId}")
    public TripResponse get(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        return tripService.getTrip(tripId, sessionId);
    }

    @PutMapping("/{tripId}/approve")
    public TripResponse approve(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        return tripService.approve(tripId, sessionId);
    }

    @GetMapping
    public List<TripResponse> getAllWithFilter(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId,
                                               @RequestParam(value = "fromStation", required = false) String fromStation,
                                               @RequestParam(value = "toStation", required = false) String toStation,
                                               @RequestParam(value = "busName", required = false) String busName,
                                               @RequestParam(value = "fromDate", required = false) String fromDate,
                                               @RequestParam(value = "toDate", required = false) String toDate) throws ServerException {
        return tripService.getTripsWithParams(fromStation, toStation, busName, fromDate, toDate, sessionId);
    }
}

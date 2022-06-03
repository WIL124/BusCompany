package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.ParamsMapper;
import thumbtack.buscompany.mapper.TripMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.RequestParams;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.service.SessionService;
import thumbtack.buscompany.service.TripService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trips")
public class TripController {
    TripService tripService;
    TripMapper tripMapper;
    SessionService sessionService;
    ParamsMapper paramsMapper;

    @PostMapping
    public Trip createTrip(@Valid @RequestBody TripRequest body,
                           @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            sessionService.updateTime(sessionId);
            return tripService.create(body);
        } else throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }

    @PutMapping("/{tripId}")
    public Trip updateTrip(@Valid @RequestBody TripRequest body,
                           @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            sessionService.updateTime(sessionId);
            return tripService.update(tripId, body);
        } else throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            sessionService.updateTime(sessionId);
            if (tripService.deleteTrip(tripId)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else throw new ServerException(ErrorCode.NOT_FOUND, "JAVASESSIONID");

        } else throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }

    @GetMapping("/{tripId}")
    public Trip get(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            sessionService.updateTime(sessionId);
            return tripService.getTrip(tripId);
        } else throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }

    @PutMapping("/{tripId}/approve")
    public Trip approve(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId, @PathVariable("tripId") int tripId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            sessionService.updateTime(sessionId);
            return tripService.approve(tripId);
        } else throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }

    @GetMapping
    public List<Trip> getAllWithFilter(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId,
                                       @RequestParam(value = "fromStation", required = false) String fromStation,
                                       @RequestParam(value = "toStation", required = false) String toStation,
                                       @RequestParam(value = "busName", required = false) String busName,
                                       @RequestParam(value = "fromDate", required = false) String fromDate,
                                       @RequestParam(value = "toDate", required = false) String toDate) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        sessionService.updateTime(sessionId);
        RequestParams params = paramsMapper.paramsFromRequest(fromDate, toDate, busName, fromStation, toStation, null);
        return tripService.getTripsWithParams(user, params);
    }
}

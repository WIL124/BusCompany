package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.service.SessionService;
import thumbtack.buscompany.service.TripService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trips")
public class TripController {
    TripService tripService;
    SessionService sessionService;

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
}

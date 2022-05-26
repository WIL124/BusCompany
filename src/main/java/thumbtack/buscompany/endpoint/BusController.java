package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.service.BusesService;
import thumbtack.buscompany.service.SessionService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/buses")
public class BusController {
    private BusesService busesService;
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<Bus>> getAll(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        if (sessionService.getUserBySessionId(sessionId) instanceof Admin) {
            sessionService.updateTime(sessionId);
            return ResponseEntity.ok().body(busesService.getAll());
        }
        throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }
}

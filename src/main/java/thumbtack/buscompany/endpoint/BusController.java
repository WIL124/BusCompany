package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.service.BusService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/buses")
public class BusController {
    private BusService busService;

    @GetMapping
    public List<Bus> getAll(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        return busService.getAll(sessionId);
    }
}

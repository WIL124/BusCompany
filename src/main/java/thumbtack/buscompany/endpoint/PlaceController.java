package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;
import thumbtack.buscompany.service.PlaceService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    PlaceService placeService;

    @GetMapping("/{orderId}")
    public List<Integer> freePlaces(@PathVariable("orderId") Integer orderId,
                                    @CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        return placeService.getFreePlaces(orderId, sessionId);
    }

    @PostMapping
    public ChoosingPlaceResponse choicePlace(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId,
                                             @RequestBody ChoosingPlaceRequest request) throws ServerException {
        return placeService.choicePlace(sessionId, request);
    }
}

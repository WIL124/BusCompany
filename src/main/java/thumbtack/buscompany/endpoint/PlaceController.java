package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;
import thumbtack.buscompany.response.FreePlacesResponse;
import thumbtack.buscompany.service.PlaceService;

import javax.validation.constraints.NotNull;

@Controller
@AllArgsConstructor
@RequestMapping("/api/places")
@ResponseStatus(HttpStatus.OK)
public class PlaceController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    private PlaceService placeService;

    @GetMapping("/{orderId}")
    public ResponseEntity<FreePlacesResponse> freePlaces(@PathVariable("orderId") Integer orderId,
                                                         @CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        return new ResponseEntity<>(placeService.getFreePlaces(orderId, sessionId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ChoosingPlaceResponse> choicePlace(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId,
                                             @RequestBody ChoosingPlaceRequest request) throws ServerException {
        return new ResponseEntity<>(placeService.choicePlace(sessionId, request), HttpStatus.OK);
    }
}

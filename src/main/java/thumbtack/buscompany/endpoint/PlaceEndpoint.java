package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.PlaceMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;
import thumbtack.buscompany.service.OrderService;
import thumbtack.buscompany.service.PlaceService;
import thumbtack.buscompany.service.SessionService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/places")
public class PlaceEndpoint {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    SessionService sessionService;
    PlaceService placeService;
    OrderService orderService;
    PlaceMapper placeMapper;

    @GetMapping("/{orderId}")
    public List<Integer> freePlaces(@PathVariable("orderId") Integer orderId,
                                    @CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        Order order = orderService.getOrderById(orderId);
        if (user instanceof Client) {
            return placeService.getFreePlaces(order.getTripDay());
        } else throw new ServerException(ErrorCode.NOT_A_CLIENT, JAVASESSIONID);
    }

    @PostMapping
    public ChoosingPlaceResponse choicePlace(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId,
                                             @RequestBody ChoosingPlaceRequest request) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        Order order = orderService.getOrderById(request.getOrderId());
        Passenger passenger = order.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(request.getFirstName()))
                .filter(p -> p.getLastName().equals(request.getLastName()))
                .filter(p -> p.getPassport().equals(request.getPassport()))
                .findFirst().orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "passenger"));
        if (user instanceof Client) {
            String ticket = placeService.choicePlace(request.getPlace(), order, passenger);
            return placeMapper.responseFromRequestAndTicket(request, ticket);
        } else throw new ServerException(ErrorCode.NOT_A_CLIENT, JAVASESSIONID);
    }
}

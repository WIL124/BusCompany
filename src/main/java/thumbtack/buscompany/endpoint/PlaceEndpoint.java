package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.service.OrderService;
import thumbtack.buscompany.service.PlaceService;
import thumbtack.buscompany.service.SessionService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/places")
public class PlaceEndpoint {
    SessionService sessionService;
    PlaceService placeService;
    OrderService orderService;

    @GetMapping("/{orderId}")
    public List<Integer> freePlaces(@PathVariable("orderId") Integer orderId,
                                    @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        Order order = orderService.getOrderById(orderId);
        if (user instanceof Client) {
            return placeService.getFreePlaces(order);
        } else throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
    }
}

package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.OrderMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.service.OrderService;
import thumbtack.buscompany.service.SessionService;
import thumbtack.buscompany.service.TripService;

import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    SessionService sessionService;
    OrderService orderService;
    OrderMapper orderMapper;
    TripService tripService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest, @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        } else {
            Trip trip = tripService.getTrip(orderRequest.getTripId());
            Order order = orderService.createOrder(orderRequest, trip);
            return orderMapper.orderToResponse(order);
        }
    }
}

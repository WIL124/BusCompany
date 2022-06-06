package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.OrderMapper;
import thumbtack.buscompany.mapper.ParamsMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.service.OrderService;
import thumbtack.buscompany.service.SessionService;
import thumbtack.buscompany.service.TripService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    SessionService sessionService;
    OrderService orderService;
    OrderMapper orderMapper;
    TripService tripService;
    ParamsMapper paramsMapper;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest, @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        } else {
            Trip trip = tripService.getTrip(orderRequest.getTripId());
            Order order = orderService.createOrder((Client) user, orderRequest, trip);
            sessionService.updateTime(sessionId);
            return orderMapper.orderToResponse(order);
        }
    }
    @GetMapping
    public List<OrderResponse> getOrdersWithParams(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId,
                                                   @RequestParam(value = "fromStation", required = false) String fromStation,
                                                   @RequestParam(value = "toStation", required = false) String toStation,
                                                   @RequestParam(value = "busName", required = false) String busName,
                                                   @RequestParam(value = "fromDate", required = false) String fromDate,
                                                   @RequestParam(value = "toDate", required = false) String toDate,
                                                   @RequestParam(value = "clientId", required = false) Integer clientId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        RequestParams params = paramsMapper.paramsFromRequest(fromDate,toDate,busName, fromStation,toStation, clientId);
        if (user instanceof Client){
            params.setClientId(null);
        }
        List<Order> orderList = orderService.getOrdersWithParams(params);
        sessionService.updateTime(sessionId);
        return orderList.stream().parallel()
                .map(order -> orderMapper.orderToResponse(order))
                .collect(Collectors.toList());
    }
}

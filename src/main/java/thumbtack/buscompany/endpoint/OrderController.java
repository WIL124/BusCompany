package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.service.OrderService;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    private OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest, @CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        return orderService.createOrder(orderRequest, sessionId);
    }

    @GetMapping
    public List<OrderResponse> getOrdersWithParams(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId,
                                                   @RequestParam(value = "fromStation", required = false) String fromStation,
                                                   @RequestParam(value = "toStation", required = false) String toStation,
                                                   @RequestParam(value = "busName", required = false) String busName,
                                                   @RequestParam(value = "fromDate", required = false) String fromDate,
                                                   @RequestParam(value = "toDate", required = false) String toDate,
                                                   @RequestParam(value = "clientId", required = false) Integer clientId) throws ServerException {
        return orderService.getOrdersWithParams(sessionId, fromStation, toStation, busName, fromDate, toDate, clientId);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Integer orderId,
                                            @CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        return orderService.deleteOrder(orderId, sessionId);
    }
}

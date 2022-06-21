package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.OrderMapper;
import thumbtack.buscompany.mapper.ParamsMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrderService extends ServiceBase {
    OrderDao orderDao;
    OrderMapper orderMapper;
    PlaceDao placeDao;
    ParamsMapper paramsMapper;

    public OrderResponse createOrder(OrderRequest orderRequest, String sessionId) throws ServerException {
        Client client = getClientOrThrow(sessionId);
        Order order = orderMapper.orderFromRequest(orderRequest, client);
        orderDao.insert(order);
        return orderMapper.orderToResponse(order);
    }

    public List<OrderResponse> getOrdersWithParams(String sessionId, String fromStation, String toStation, String busName,
                                                   String fromDate, String toDate, Integer clientId) throws ServerException {

        User user = getUserOrThrow(sessionId);
        RequestParams params = paramsMapper.paramsFromRequest(fromDate, toDate, busName, fromStation, toStation, clientId);
        if (user instanceof Client) {
            params.setClientId(user.getId());
        }
        List<Order> dirtyOrders = orderDao.getAllByClientId(params.getClientId());
        dirtyOrders = dirtyOrders.stream().parallel()
                .filter(
                        busNameFilter(params.getBusName())
                                .and(fromStationFilter(params.getFromStation()))
                                .and(toStationFilter(params.getToStation()))
                                .and(fromDateFilter(params.getFromDate()))
                                .and(toDateFilter(params.getToDate())))
                .collect(Collectors.toList());
        return orderMapper.ordersToResponses(dirtyOrders);
    }

    public ResponseEntity<Void> deleteOrder(int orderId, String sessionId) throws ServerException {
        Client client = getClientOrThrow(sessionId);
        if (client.getOrders() == null) {
            throw new ServerException(ErrorCode.ORDER_NOT_FOUND, "orderId");
        }
        Order order = client.getOrders().stream().filter(o -> o.getOrderId() == orderId).findFirst().orElseThrow(() -> new ServerException(ErrorCode.ORDER_NOT_FOUND, "orderId"));
        order.getPassengers().parallelStream().forEach(passenger -> placeDao.removePassenger(passenger));
        orderDao.delete(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Predicate<Order> busNameFilter(String busName) {
        return order -> order.getTripDay().getTrip().getBus().getBusName().equals(busName);
    }

    private Predicate<Order> fromStationFilter(String fromStation) {
        return order -> order.getTripDay().getTrip().getFromStation().equals(fromStation);
    }

    private Predicate<Order> toStationFilter(String toStation) {
        return order -> order.getTripDay().getTrip().getToStation().equals(toStation);
    }

    private Predicate<Order> fromDateFilter(LocalDate fromDate) {
        return order -> order.getTripDay().getDate().isAfter(fromDate);
    }

    private Predicate<Order> toDateFilter(LocalDate toDate) {
        return order -> order.getTripDay().getDate().isBefore(toDate);
    }
}

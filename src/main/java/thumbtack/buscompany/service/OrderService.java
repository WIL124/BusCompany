package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.OrderMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    OrderDao orderDao;
    SessionDao sessionDao;
    OrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest orderRequest, String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID"))
                .getUser();
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        }
        Client client = (Client) user;
        Order order = orderMapper.orderFromRequest(orderRequest, client);
        orderDao.insert(order);
        sessionDao.updateTime(sessionId);
        return orderMapper.orderToResponse(order);
    }

    public List<Order> getOrdersWithParams(RequestParams params) {
        List<Order> dirtyOrders = orderDao.getAllByClientId(params.getClientId());
        return dirtyOrders.stream().parallel()
                .filter(
                        busNameFilter(params.getBusName())
                                .and(fromStationFilter(params.getFromStation()))
                                .and(toStationFilter(params.getToStation()))
                                .and(fromDateFilter(params.getFromDate()))
                                .and(toDateFilter(params.getToDate())))
                .collect(Collectors.toList());
    }

    public Order getOrderById(Integer orderId) throws ServerException {
        return orderDao.getById(orderId).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "orderId"));
    }

    public void deleteOrder(Order order) {
        orderDao.delete(order);
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

package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.OrderMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.RequestParams;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.request.OrderRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    OrderDao orderDao;
    OrderMapper orderMapper;

    public Order createOrder(Client client, OrderRequest orderRequest) throws ServerException {
        Order order = orderMapper.orderFromRequest(orderRequest, client);
        orderDao.insert(order);
        return order;
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

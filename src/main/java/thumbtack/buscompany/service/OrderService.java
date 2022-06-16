package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.dao.SessionDao;
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
public class OrderService {
    OrderDao orderDao;
    SessionDao sessionDao;
    OrderMapper orderMapper;
    PlaceDao placeDao;
    ParamsMapper paramsMapper;

    public OrderResponse createOrder(OrderRequest orderRequest, String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID"))
                .getUser();
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        }
        Client client = (Client) user;
        Order order = orderMapper.orderFromRequest(orderRequest, client);
        TripDay tripDay = order.getTripDay();
        if (isEnoughSeats(tripDay, order)) {
            orderDao.insert(order);
        } else throw new ServerException(ErrorCode.NOT_ENOUGH_SEATS, "passengers");
        sessionDao.updateTime(sessionId);
        return orderMapper.orderToResponse(order);
    }

    public List<OrderResponse> getOrdersWithParams(String sessionId, String fromStation, String toStation, String busName,
                                                   String fromDate, String toDate, Integer clientId) throws ServerException {

        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        RequestParams params = paramsMapper.paramsFromRequest(fromDate, toDate, busName, fromStation, toStation, clientId);
        if (user instanceof Client) {
            params.setClientId(null);
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
        sessionDao.updateTime(sessionId);
        return orderMapper.ordersToResponses(dirtyOrders);
    }

    public Order getOrderById(Integer orderId) throws ServerException {
        return orderDao.getById(orderId).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "orderId"));
    }

    public ResponseEntity<Void> deleteOrder(Integer orderId, String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID"))
                .getUser();
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        }
        Client client = (Client) user;
        Order order = orderDao.getById(orderId).orElseThrow(() -> new ServerException(ErrorCode.ORDER_NOT_FOUND, "orderId"));
        if (order.getClient().equals(client)) {
            order.getPassengers().parallelStream().forEach(passenger -> placeDao.removePassenger(passenger));
            orderDao.delete(order);
            sessionDao.updateTime(sessionId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else throw new ServerException(ErrorCode.ORDER_NOT_FOUND, orderId.toString());
    }

    private boolean isEnoughSeats(TripDay tripDay, Order order) {
        return tripDay.getOrders() != null ?
                (tripDay.getTrip().getBus().getPlaceCount() - tripDay.getOrders().stream().mapToInt(o -> o.getPassengers().size()).sum())
                        >= order.getPassengers().size() :
                tripDay.getTrip().getBus().getPlaceCount() >= order.getPassengers().size();
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

package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.PlaceMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {
    PlaceDao placeDao;
    SessionDao sessionDao;
    OrderDao orderDao;
    PlaceMapper placeMapper;

    public List<Integer> getFreePlaces(Integer orderId, String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        Order order = orderDao.getById(orderId).orElseThrow(() -> new ServerException(ErrorCode.ORDER_NOT_FOUND, "orderId"));
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        }
        int totalPlaces = order.getTripDay().getTrip().getBus().getPlaceCount();
        List<Integer> places = new ArrayList<>();
        for (int i = 1; i <= totalPlaces; i++) {
            places.add(i);
        }
        places.removeAll(placeDao.getBookedPlaces(order.getTripDay()));
        sessionDao.updateTime(sessionId);
        return places;
    }

    public ChoosingPlaceResponse choicePlace(String sessionId, ChoosingPlaceRequest request) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        Order order = orderDao.getById(request.getOrderId()).orElseThrow(() -> new ServerException(ErrorCode.ORDER_NOT_FOUND, "orderId"));
        Passenger passenger = order.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(request.getFirstName()))
                .filter(p -> p.getLastName().equals(request.getLastName()))
                .filter(p -> p.getPassport().equals(request.getPassport()))
                .findFirst().orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "passenger"));
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        }
        if (order.getTripDay().getTrip().getBus().getPlaceCount() < request.getPlace()) {
            throw new ServerException(ErrorCode.INVALID_PLACE, "place");
        }
        placeDao.choicePlace(passenger, request.getPlace());
        String ticket = "Билет " + order.getTripDay().getTripDayId() + "_" + request.getPlace();
        sessionDao.updateTime(sessionId);
        return placeMapper.responseFromRequestAndTicket(request, ticket);
    }
}

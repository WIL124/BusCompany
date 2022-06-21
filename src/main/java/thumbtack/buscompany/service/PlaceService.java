package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.PlaceMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;
import thumbtack.buscompany.response.FreePlacesResponse;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class PlaceService extends ServiceBase {
    PlaceDao placeDao;
    PlaceMapper placeMapper;

    public FreePlacesResponse getFreePlaces(Integer orderId, String sessionId) throws ServerException {
        Client client = getClientOrThrow(sessionId);
        Order order = getClientsOrderOrThrow(client, orderId);
        List<Integer> freePlaces = placeDao.getFreePlaces(order.getTripDay());
        return new FreePlacesResponse(freePlaces);
    }

    public ChoosingPlaceResponse choicePlace(String sessionId, ChoosingPlaceRequest request) throws ServerException {
        Client client = getClientOrThrow(sessionId);
        Order order = getClientsOrderOrThrow(client, request.getOrderId());
        Passenger passenger = order.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(request.getFirstName()))
                .filter(p -> p.getLastName().equals(request.getLastName()))
                .filter(p -> p.getPassport().equals(request.getPassport()))
                .findFirst().orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "passenger"));
        if (order.getTripDay().getTrip().getBus().getPlaceCount() < request.getPlace()) {
            throw new ServerException(ErrorCode.INVALID_PLACE, "place");
        }
        if (!placeDao.choicePlace(order.getTripDay(), passenger, request.getPlace())) {
            throw new ServerException(ErrorCode.CANT_CHOICE_PLACE, "place");
        }
        String ticket = "Билет_" + order.getTripDay().getTripDayId() + "_" + request.getPlace();
        return placeMapper.responseFromRequestAndTicket(request, ticket);
    }
}

package thumbtack.buscompany.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import thumbtack.buscompany.dao.TripDayDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", uses = TripDayDao.class)
@AllArgsConstructor
@NoArgsConstructor
public abstract class OrderMapper {
    @Autowired
    protected TripDayDao tripDayDao;

    @Mapping(target = "tripId", source = "order.tripDay.trip.tripId")
    @Mapping(target = "fromStation", source = "order.tripDay.trip.fromStation")
    @Mapping(target = "toStation", source = "order.tripDay.trip.toStation")
    @Mapping(target = "busName", source = "order.tripDay.trip.bus.busName")
    @Mapping(target = "start", source = "order.tripDay.trip.start")
    @Mapping(target = "duration", source = "order.tripDay.trip.duration")
    @Mapping(target = "price", source = "order.tripDay.trip.price")
    @Mapping(target = "totalPrice", expression = "java(order.getTripDay().getTrip().getPrice().multiply(BigDecimal.valueOf(order.getPassengers().size())))")
    @Mapping(target = "date", source = "order.tripDay.date")
    public abstract OrderResponse orderToResponse(Order order);

    public abstract List<OrderResponse> ordersToResponses(List<Order> orders);

    @Mapping(target = "tripDay", resultType = TripDay.class, source = "request", qualifiedByName= "tripDay")
    public abstract Order orderFromRequest(OrderRequest request, Client client) throws ServerException;

    @Named("tripDay")
    TripDay getTripDayFromRequest(OrderRequest request) throws ServerException {
        return tripDayDao.getTripDayByTripIdAndDate(request.getTripId(), dateFromString(request.getDate()))
                .orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "trip, date"));
    }

    public LocalDate dateFromString(String str) {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}

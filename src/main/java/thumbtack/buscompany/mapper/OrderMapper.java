package thumbtack.buscompany.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.service.TripDayService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = TripDayService.class)
@AllArgsConstructor
@NoArgsConstructor
public abstract class OrderMapper {
    @Autowired
    protected TripDayService tripDayService;

    @Mapping(target = "tripId", source = "order.tripDay.trip.tripId")
    @Mapping(target = "fromStation", source = "order.tripDay.trip.fromStation")
    @Mapping(target = "toStation", source = "order.tripDay.trip.toStation")
    @Mapping(target = "busName", source = "order.tripDay.trip.bus.busName")
    @Mapping(target = "start", source = "order.tripDay.trip.start")
    @Mapping(target = "duration", source = "order.tripDay.trip.duration")
    @Mapping(target = "price", source = "order.tripDay.trip.price")
    @Mapping(target = "totalPrice", expression = "java(order.getTripDay().getTrip().getPrice()*order.getPassengers().size())")
    public abstract OrderResponse orderToResponse(Order order);

    @Mapping(target = "tripDay", resultType = TripDay.class,
            expression = "java(tripDayService.getTripDayByTripIdAndDate(request.getTripId(), dateFromString(request.getDate())))")
    public abstract Order orderFromRequest(OrderRequest request, Client client) throws ServerException;

    public LocalDate dateFromString(String str) {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}

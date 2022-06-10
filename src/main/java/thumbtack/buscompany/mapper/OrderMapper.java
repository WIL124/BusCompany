package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.service.TripService;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR, uses = TripService.class)
public interface OrderMapper {
    @Mapping(target = "tripId", source = "order.tripDay.trip.tripId")
    @Mapping(target = "fromStation", source = "order.tripDay.trip.fromStation")
    @Mapping(target = "toStation", source = "order.tripDay.trip.toStation")
    @Mapping(target = "busName", source = "order.tripDay.trip.bus.busName")
    @Mapping(target = "start", source = "order.tripDay.trip.start")
    @Mapping(target = "duration", source = "order.tripDay.trip.duration")
    @Mapping(target = "price", source = "order.tripDay.trip.price")
    @Mapping(target = "totalPrice", expression = "java(order.getTripDay().getTrip().getPrice()*order.getPassengers().size())")
    OrderResponse orderToResponse(Order order);

    @Mapping(target = "date", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "tripDay", expression = "java(tripDayService.getTripDayByTripIdAndDate(request.getTripId(), request.getDate()))")
    Order orderFromRequest(OrderRequest request, Client client) throws ServerException;
}

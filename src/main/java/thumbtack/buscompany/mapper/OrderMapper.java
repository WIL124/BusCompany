package thumbtack.buscompany.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.request.OrderRequest;
import thumbtack.buscompany.response.OrderResponse;
import thumbtack.buscompany.service.TripService;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR)
@AllArgsConstructor
@NoArgsConstructor
public abstract class OrderMapper {
    TripService tripService;
    @Mapping(target = "tripId", source = "order.trip.tripId")
    @Mapping(target = "fromStation", source = "order.trip.fromStation")
    @Mapping(target = "toStation", source = "order.trip.toStation")
    @Mapping(target = "busName", source = "order.trip.bus.busName")
    @Mapping(target = "start", source = "order.trip.start")
    @Mapping(target = "duration", source = "order.trip.duration")
    @Mapping(target = "price", source = "order.trip.price")
    @Mapping(target = "totalPrice", expression = "java(order.getTrip().getPrice()*order.getPassengers().size())")
    public abstract OrderResponse orderToResponse(Order order) ;
    @Mapping(target = "date", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "trip", expression = "java(tripService.getTrip(request.getTripId()))")
    public abstract Order orderFromRequest(OrderRequest request, Client client) throws ServerException;
}

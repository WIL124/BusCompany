package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.response.OrderResponse;

@Mapper
public interface OrderMapper {
    @Mapping(target = "tripId", source = "trip.tripId")
    @Mapping(target = "fromStation", source = "trip.fromStation")
    @Mapping(target = "toStation", source = "trip.toStation")
    @Mapping(target = "busName", source = "trip.bus.busName")
    @Mapping(target = "start", source = "trip.start")
    @Mapping(target = "duration", source = "trip.duration")
    @Mapping(target = "price", source = "trip.price")
    @Mapping(target = "totalPrice", expression = "java(price*passengers.size())")
    OrderResponse orderToResponse(Order order);
}

package thumbtack.buscompany.mapper;

import org.mapstruct.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.model.Schedule;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.request.ScheduleDto;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.TripResponse;
import thumbtack.buscompany.service.BusService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", uses = BusService.class, imports = BigDecimal.class)
public interface TripMapper {

    @Mapping(target = "approved", constant = "false")
    @Mapping(target = "bus", source = "busName", resultType = Bus.class)
    @Mapping(target = "schedule", source = "scheduleDto")
    @Mapping(target = "tripDays", expression = "java(new ArrayList<>())")
    @Mapping(target = "price", expression = "java(BigDecimal.valueOf(request.getPrice()).setScale(2))")
    @Named(value = "trip")
    Trip tripFromRequest(TripRequest request) throws ServerException;

    @Mapping(target = "schedule", source = "scheduleDto")
    @Mapping(target = "price", expression = "java(BigDecimal.valueOf(tripRequest.getPrice()).setScale(2))")
    void updateTrip(@MappingTarget Trip trip, TripRequest tripRequest);

    @Mapping(target = "dates", source = "trip.tripDays", qualifiedByName = "datesFromTripDays")
    @Mapping(target = "schedule", source = "schedule")
    @Mapping(target = "price", expression = "java(trip.getPrice().setScale(2))")
    TripResponse tripToResponse(Trip trip);

    @Named("datesFromTripDays")
    List<LocalDate> datesFromTripDays(List<TripDay> tripDays);

    default LocalDate dateFromTripDay(TripDay tripDay) {
        return tripDay.getDate();
    }

    List<TripResponse> tripResponseListFromTrips(List<Trip> trips);

    @IterableMapping(dateFormat = "yyyy.MM.dd")
    List<LocalDate> datesFromString(List<String> strings);

    @Mapping(target = "fromDate", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "toDate", dateFormat = "yyyy.MM.dd")
    Schedule scheduleFromDto(ScheduleDto scheduleDto);
}

package thumbtack.buscompany.mapper;

import org.mapstruct.*;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.model.Schedule;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.request.ScheduleDto;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.TripResponse;
import thumbtack.buscompany.service.BusService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR, uses = BusService.class)
public interface TripMapper {

    @Mapping(target = "approved", constant = "false")
    @Mapping(target = "bus", source = "busName", resultType = Bus.class)
    @Mapping(target = "schedule", source = "scheduleDto")
    @Mapping(target = "tripDays", expression = "java(new ArrayList<>())")
    @Named(value = "trip")
    Trip tripFromRequest(TripRequest request);

    @Mapping(target = "schedule", source = "scheduleDto")
    void updateTrip(@MappingTarget Trip trip, TripRequest tripRequest);

    @Mapping(target = "dates", source = "trip.tripDays", qualifiedByName = "datesFromTripDays")
    @Mapping(target = "schedule", source = "schedule")
    TripResponse tripToResponse(Trip trip);

    default LocalDate dateFromTripDay(TripDay tripDay) {
        return tripDay.getDate();
    }

    @Named("datesFromTripDays")
    List<LocalDate> datesFromTripDays(List<TripDay> tripDays);

    List<TripResponse> tripResponseListFromTrips(List<Trip> trips);

    default void createTripDays(@MappingTarget Trip trip, List<LocalDate> dates) {
        for (LocalDate date : dates) {
            trip.getTripDays().add(new TripDay(date, trip, new ArrayList<>()));
        }
    }

    @Mapping(target = "date", source = "date", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "orders", expression = "java(new ArrayList())")
    TripDay tripDayFromDate(String date);

    @IterableMapping(dateFormat = "yyyy.MM.dd")
    List<LocalDate> datesFromString(List<String> strings);


    @Mapping(target = "fromDate", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "toDate", dateFormat = "yyyy.MM.dd")
    Schedule scheduleFromDto(ScheduleDto scheduleDto);

    @InheritInverseConfiguration
    ScheduleDto scheduleToDto(Schedule schedule);
}

package thumbtack.buscompany.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Schedule;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.RequestParams;
import thumbtack.buscompany.request.ScheduleDto;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.service.BusService;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR)
@NoArgsConstructor
@AllArgsConstructor
public abstract class TripMapper {
    @Autowired
    protected BusService busService;

    @Mapping(target = "dates", source = "dates", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "approved", constant = "false")
    @Mapping(target = "bus", expression = "java(busService.get(request.getBusName()))")
    @Mapping(target = "schedule", source = "scheduleDto")
    public abstract Trip tripFromRequest(TripRequest request) throws ServerException;

    @IterableMapping(dateFormat = "yyyy.MM.dd")
    public abstract List<LocalDate> datesFromString(List<String> strings);

    @Mapping(target = "fromDate", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "toDate", dateFormat = "yyyy.MM.dd")
    public abstract Schedule scheduleFromDto(ScheduleDto scheduleDto);
}

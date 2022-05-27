package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.TripMapper;
import thumbtack.buscompany.model.Schedule;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.request.TripRequest;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class TripService {
    private TripMapper tripMapper;

    public Trip create(TripRequest tripRequest) throws ServerException {

        Trip trip = tripMapper.tripFromRequest(tripRequest);
        if (trip.getSchedule() != null) {
            LocalDate[] dates = createDatesFromSchedule(trip.getSchedule());
            trip.setDates(dates);
        }
        return trip;
    }

    private LocalDate[] createDatesFromSchedule(Schedule schedule) {
        //TODO
        return null;
    }
}

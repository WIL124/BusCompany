package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.TripDay;

import java.time.LocalDate;
import java.util.Optional;

public interface TripDayDao {
    Optional<TripDay> getTripDayByTripIdAndDate(Integer tripId, LocalDate date);
}

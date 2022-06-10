package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripDay;

import java.time.LocalDate;
import java.util.List;

public interface PlaceDao {
    List<Integer> getBookedPlaces(TripDay tripDay);

    void choicePlace(Passenger passenger, Integer place);
}

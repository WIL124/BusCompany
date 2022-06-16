package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.TripDay;

import java.util.List;

public interface PlaceDao {
    List<Integer> getFreePlaces(TripDay tripDay);

    boolean choicePlace(TripDay tripDay, Passenger passenger, Integer place);
}

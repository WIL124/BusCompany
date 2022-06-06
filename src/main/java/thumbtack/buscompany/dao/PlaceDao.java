package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Trip;

import java.time.LocalDate;
import java.util.List;

public interface PlaceDao {
    List<Integer> getBookedPlaces(Trip trip, LocalDate date);
}

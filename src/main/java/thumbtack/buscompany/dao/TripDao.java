package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripParams;
import thumbtack.buscompany.model.User;

import java.util.List;
import java.util.Optional;

public interface TripDao {
    void insert(Trip trip);

    Optional<Trip> getTrip(int tripId);

    boolean update(int tripId, Trip body);

    boolean deleteTrip(int tripId);

    boolean approve(int tripId);

    List<Trip> getTripsWithParams(User user, TripParams paramsFromRequest);
}

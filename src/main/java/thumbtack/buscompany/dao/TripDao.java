package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Trip;

import java.util.Optional;

public interface TripDao {
    void insert(Trip trip);

    Optional<Trip> getTrip(int tripId);

    boolean update(int tripId, Trip body);

    boolean deleteTrip(int tripId);

    boolean approve(int tripId);
}

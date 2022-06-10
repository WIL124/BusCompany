package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.RequestParams;
import thumbtack.buscompany.model.User;

import java.util.List;
import java.util.Optional;

public interface TripDao {
    void insert(Trip trip);

    Optional<Trip> getTrip(int tripId);

    boolean update(Trip body);

    boolean deleteTrip(int tripId);

    boolean approve(Trip trip);

    List<Trip> getTripsWithParams(User user, RequestParams paramsFromRequest);
}

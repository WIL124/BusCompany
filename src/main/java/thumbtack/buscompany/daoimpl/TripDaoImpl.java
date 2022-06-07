package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.model.RequestParams;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.repository.TripRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TripDaoImpl implements TripDao {
    TripRepository tripRepository;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void insert(Trip trip) {
        tripRepository.insertTrip(trip);
        trip.getDates().forEach(date -> tripRepository.insertTripDate(trip, date));
    }

    @Override
    public Optional<Trip> getTrip(int tripId) {
        return Optional.ofNullable(tripRepository.getTrip(tripId));
    }

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public boolean update(Trip trip) {
        boolean first = tripRepository.updateTripProperties(trip);
        if (!first) {
            return false;
        }
        boolean second = tripRepository.deleteAllTripDates(trip.getTripId());
        //TODO may be do it Async?
        trip.getDates().forEach(date -> tripRepository.insertTripDate(trip, date));
        return second;
    }

    @Override
    public boolean deleteTrip(int tripId) {
        return tripRepository.deleteTrip(tripId);
    }

    @Override
    public boolean approve(int tripId) {
        return tripRepository.approve(tripId);
    }

    @Override
    public List<Trip> getTripsWithParams(User user, RequestParams params) {
        return tripRepository.getTripsWithParams(user, params);
    }
}

package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.repository.TripRepository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class TripDaoImpl implements TripDao {
    TripRepository tripRepository;

    @Override
    @Transactional
    public void insert(Trip trip) {
        tripRepository.insertTrip(trip);
        trip.getDates().forEach(date -> tripRepository.insertTripDate(trip, date));
    }

    @Override
    public Optional<Trip> getTrip(int tripId) {
        return Optional.ofNullable(tripRepository.getTrip(tripId));
    }

    @Override
    @Transactional
    public boolean update(int tripId, Trip body) {
        boolean first = tripRepository.updateTripProperties(tripId, body);
        boolean second = tripRepository.deleteAllTripDates(tripId);
        //TODO may be do it Async?
        body.getDates().forEach(date -> tripRepository.insertTripDate(body, date));
        return first && second;
    }

    @Override
    public boolean deleteTrip(int tripId) {
        return tripRepository.deleteTrip(tripId);
    }
}

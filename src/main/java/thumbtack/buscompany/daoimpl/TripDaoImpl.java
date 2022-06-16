package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.model.RequestParams;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.repository.PlaceRepository;
import thumbtack.buscompany.repository.TripDayRepository;
import thumbtack.buscompany.repository.TripRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class TripDaoImpl implements TripDao {

    @Autowired
    TripRepository tripRepository;
    @Autowired
    TripDayRepository tripDayRepository;
    @Autowired
    PlaceRepository placeRepository;

    @Override
    public void insert(Trip trip) {
        tripRepository.insertTrip(trip);
        trip.getTripDays().forEach(tripDay -> tripDayRepository.insertTripDay(tripDay));
    }

    @Override
    public Optional<Trip> getTrip(int tripId) {
        return Optional.ofNullable(tripRepository.getTripById(tripId));
    }

    @Override
    public boolean update(Trip trip) {
        if (tripRepository.updateTripProperties(trip) == 0) return false;
        tripDayRepository.deleteAllTripDays(trip.getTripId());
        trip.getTripDays().forEach(tripDay -> tripDayRepository.insertTripDay(tripDay));
        return true;
    }

    @Override
    public boolean deleteTrip(int tripId) {
        return tripRepository.deleteTrip(tripId);
    }

    @Override
    public boolean approve(Trip trip) {
        trip.getTripDays().parallelStream().forEach(tripDay -> placeRepository.insertPlaces(tripDay, trip.getBus().getPlaceCount()));
        return tripRepository.approve(trip.getTripId());
    }

    @Override
    public List<Trip> getTripsWithParams(User user, RequestParams params) {
        return tripRepository.getTripsWithParams(user, params);
    }
}

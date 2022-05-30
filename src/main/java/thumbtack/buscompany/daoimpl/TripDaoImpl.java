package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.repository.TripRepository;

@Repository
@AllArgsConstructor
public class TripDaoImpl implements TripDao {
    TripRepository tripRepository;

    @Override
    public void insert(Trip trip) {
        tripRepository.insertTrip(trip);
        trip.getDates().forEach(e -> tripRepository.insertTripDate(trip, e));
    }
}

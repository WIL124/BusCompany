package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.repository.OrderRepository;
import thumbtack.buscompany.repository.PlaceRepository;

import java.util.List;

@Repository
@AllArgsConstructor
@Transactional
public class PlaceDaoImpl implements PlaceDao {
    PlaceRepository placeRepository;
    OrderRepository orderRepository;

    @Override
    public List<Integer> getFreePlaces(TripDay tripDay) {
        return placeRepository.getFreePlaces(tripDay);
    }

    @Override
    public boolean choicePlace(TripDay tripDay, Passenger passenger, Integer place) {
        placeRepository.removePassengerFromPlace(tripDay, passenger);
        return placeRepository.choicePlace(tripDay, passenger, place) != 0;
    }
}

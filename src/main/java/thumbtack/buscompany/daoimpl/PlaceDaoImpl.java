package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.repository.OrderRepository;
import thumbtack.buscompany.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    PlaceRepository placeRepository;
    OrderRepository orderRepository;

    @Override
    public List<Integer> getBookedPlaces(TripDay tripDay) {
        return placeRepository.getBookedPlaces(tripDay);
    }

    @Override
    public void choicePlace(Passenger passenger, Integer place) {
        placeRepository.choicePlace(passenger, place);
    }
}

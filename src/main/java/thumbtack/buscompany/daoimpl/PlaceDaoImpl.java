package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.repository.OrderRepository;
import thumbtack.buscompany.repository.PlaceRepository;
import thumbtack.buscompany.request.ChoosingPlaceRequest;

import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor
public class PlaceDaoImpl implements PlaceDao {
    PlaceRepository placeRepository;
    OrderRepository orderRepository;
    @Override
    public List<Integer> getBookedPlaces(Trip trip, LocalDate date) {
        return placeRepository.getBookedPlaces(trip, date);
    }

    @Override
    public boolean updatePlace(Integer place, Order order, Passenger passenger) {
        int passengerId = orderRepository.getPassengerIdByPassport(passenger.getPassport());
//        int tripDateId = orderRepository.get
        return placeRepository.updatePlace(place, order, passengerId);
    }
}

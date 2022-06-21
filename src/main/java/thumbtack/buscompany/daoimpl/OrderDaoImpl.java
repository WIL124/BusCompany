package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.repository.OrderRepository;
import thumbtack.buscompany.repository.PassengersRepository;
import thumbtack.buscompany.repository.TripDayRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderDaoImpl implements OrderDao {
    OrderRepository orderRepository;
    TripDayRepository tripDayRepository;
    PassengersRepository passengersRepository;

    @Override
    public void insert(Order order) throws ServerException {
        if (tripDayRepository.reducePlaces(order) == 0) {
            throw new ServerException(ErrorCode.NOT_ENOUGH_SEATS, "passengers");
        }
        orderRepository.insert(order);
        order.getPassengers().forEach(passenger -> passengersRepository.insertPassenger(order, passenger));
    }

    @Override
    public List<Order> getAllByClientId(Integer clientId) {
        return orderRepository.getAllByClientId(clientId);
    }

    @Override
    public Optional<Order> getById(Integer orderId) {
        return Optional.ofNullable(orderRepository.getById(orderId));
    }

    @Override
    public void delete(Order order) throws ServerException {
        orderRepository.deleteOrder(order);
        tripDayRepository.increasePlaces(order);
    }
}

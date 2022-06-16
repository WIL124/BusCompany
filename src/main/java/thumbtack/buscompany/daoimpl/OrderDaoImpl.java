package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(rollbackFor = {RuntimeException.class, ServerException.class})
public class OrderDaoImpl implements OrderDao {
    OrderRepository orderRepository;
    TripDayRepository tripDayRepository;
    PassengersRepository passengersRepository;

    @Override
    public void insert(Order order) throws ServerException {
        if (orderRepository.insert(order) == 0) throw new ServerException(ErrorCode.OLD_INFO, "passengers");
        tripDayRepository.updateVersion(order.getTripDay());
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
    public void delete(Order order) {
        orderRepository.deleteOrder(order);
        tripDayRepository.updateVersion(order.getTripDay());
    }
}

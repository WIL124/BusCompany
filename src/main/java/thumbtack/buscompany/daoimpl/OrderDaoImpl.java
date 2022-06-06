package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.repository.OrderRepository;
import thumbtack.buscompany.repository.PassengersRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderDaoImpl implements OrderDao {
    OrderRepository orderRepository;
    PassengersRepository passengersRepository;

    @Override
    @Transactional(rollbackFor = SQLException.class)
    public void insert(Order order) {
        orderRepository.insert(order);
        passengersRepository.insertPassengers(order);
    }

    @Override
    public List<Order> getAllByClientId(Integer clientId) {
        return orderRepository.getAllByClientId(clientId);
    }

    @Override
    public Optional<Order> getById(Integer orderId) {
        return Optional.ofNullable(orderRepository.getById(orderId));
    }
}

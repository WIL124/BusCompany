package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.repository.OrderRepository;

@Repository
@AllArgsConstructor
public class OrderDaoImpl implements OrderDao {
    OrderRepository orderRepository;
    @Override
    public void insert(Order order) {
        orderRepository.insert(order);
        orderRepository.insertPassengers(order);
    }
}

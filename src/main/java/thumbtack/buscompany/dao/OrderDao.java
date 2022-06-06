package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    void insert(Order order);

    List<Order> getAllByClientId(Integer clientId);

    Optional<Order> getById(Integer orderId);
}

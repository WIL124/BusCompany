package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Order;

import java.util.List;

public interface OrderDao {
    void insert(Order order);

    List<Order> getAllByClientId(Integer clientId);
}

package thumbtack.buscompany.dao;

import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    void insert(Order order) throws ServerException;

    List<Order> getAllByClientId(Integer clientId);

    Optional<Order> getById(Integer orderId);

    void delete(Order order);
}

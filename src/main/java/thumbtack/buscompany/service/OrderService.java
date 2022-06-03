package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.OrderMapper;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.request.OrderRequest;

@Service
@AllArgsConstructor
public class OrderService {
    OrderDao orderDao;
    OrderMapper orderMapper;

    public Order createOrder(OrderRequest orderRequest, Trip trip) throws ServerException {
        Order order = orderMapper.orderFromRequest(orderRequest);
        order.setTrip(trip);
        orderDao.insert(order);
        return order;
    }
}

package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.OrderDao;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.request.OrderRequest;

@Service
@AllArgsConstructor
public class OrderService {
    OrderDao orderDao;
    public Order createOrder(OrderRequest request) {

        return null;
    }
}

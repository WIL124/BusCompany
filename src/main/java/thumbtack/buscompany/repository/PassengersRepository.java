package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;

@Mapper
@Repository
public interface PassengersRepository {
    @InsertProvider(type = SqlProvider.class, method = "insertAllPassengers")
    void insertPassengers(@Param("order") Order order);

    class SqlProvider {
        public static String insertAllPassengers(Order order) {
            String orderId = Integer.toString(order.getOrderId());
            return new SQL() {
                {
                    INSERT_INTO("passengers");
                    for (Passenger passenger : order.getPassengers()) {
                        VALUES("orderId", orderId);
                        VALUES("firstName", passenger.getFirstName());
                        VALUES("lastName", passenger.getLastName());
                        VALUES("passport", Integer.toString(passenger.getPassport()));
                    }
                }
            }.toString();
        }
    }
}

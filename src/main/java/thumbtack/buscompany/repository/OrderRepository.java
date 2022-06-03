package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Trip;

import java.util.List;

@Mapper
@Repository
public interface OrderRepository {
    @Insert("INSERT INTO orders (tripId, date, clientId) " +
            "VALUES (#{order.trip.tripId}, #{order.date}, #{order.client.id})")
    @Options(useGeneratedKeys = true, keyProperty = "order.orderId")
    void insert(@Param("order") Order order);

    @Select("SELECT orderId, tripId, date FROM orders WHERE clientId = #{id}")
    @Results(
            @Result(property = "trip", column = "tripId", javaType = Trip.class,
                    one = @One(select = "thumbtack.buscompany.repository.TripRepository.getTrip"))
    )
    List<Order> getAllByClientId(@Param("id") Integer id);
}

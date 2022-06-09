package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Trip;

import java.util.List;

@Mapper
@Repository
public interface OrderRepository {

    @Select("SELECT orderId, tripId, date, clientId " +
            "FROM orders o " +
            "JOIN trips_dates td on td.id = o.trips_dates_id " +
            "WHERE clientId = #{id}")
    @Results(id = "order", value = {
            @Result(property = "trip", column = "tripId", javaType = Trip.class,
                    one = @One(select = "thumbtack.buscompany.repository.TripRepository.getTrip")),
            @Result(property = "client", column = "clientId", javaType = Client.class,
                    one = @One(select = "thumbtack.buscompany.repository.UserRepository.getUserById"))}
    )
    List<Order> getAllByClientId(@Param("id") Integer id);

    @Select("SELECT orderId, tripId, date, clientId " +
            "FROM orders o " +
            "JOIN trips_dates td on td.id = o.trips_dates_id " +
            "WHERE orderId = #{orderId}")
    @ResultMap("order")
    Order getById(Integer orderId);

    @Insert("INSERT INTO orders (trips_dates_id, clientId) " +
            "SELECT id, #{order.client.id} " +
            "FROM trips_dates " +
            "WHERE tripId=#{order.trip.tripId} AND date = #{order.date}")
    @Options(useGeneratedKeys = true, keyProperty = "order.orderId")
    void insert(@Param("order") Order order);

    @Delete("DELETE FROM orders WHERE orderId=#{order.orderId}")
    boolean deleteOrder(@Param("order") Order order);
}

package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.TripDay;

import java.util.List;

@Mapper
public interface OrderRepository {

    @Select("SELECT id, trips_dates_id AS tripDayId, clientId " +
            "FROM orders " +
            "WHERE clientId = #{id}")
    @Results(id = "order", value = {
            @Result(property = "orderId", column = "id"),
            @Result(property = "tripDay", column = "tripDayId", javaType = TripDay.class,
                    one = @One(select = "thumbtack.buscompany.repository.TripDayRepository.getTripDayById", fetchType = FetchType.LAZY)),
            @Result(property = "client", column = "clientId", javaType = Client.class,
                    one = @One(select = "thumbtack.buscompany.repository.UserRepository.getUserById", fetchType = FetchType.LAZY)),
            @Result(property = "passengers", column = "id", javaType = List.class,
                    many = @Many(select = "thumbtack.buscompany.repository.PassengersRepository.getAllByOrderId", fetchType = FetchType.LAZY))}
    )
    List<Order> getAllByClientId(@Param("id") Integer id);

    @Select("SELECT id, trips_dates_id AS tripDayId, clientId " +
            "FROM orders " +
            "WHERE id = #{orderId}")
    @ResultMap("order")
    Order getById(@Param("orderId") Integer orderId);

    @Select("SELECT id, trips_dates_id AS tripDayId, clientId " +
            "FROM orders " +
            "WHERE trips_dates_id = #{tripDayId}")
    @ResultMap("order")
    Order getByTripDayId(@Param("tripDayId") Integer tripDayId);

    @Insert("INSERT INTO orders (trips_dates_id, clientId) " +
            "SELECT id, #{order.client.id} " +
            "FROM trips_dates " +
            "WHERE tripId = #{order.tripDay.trip.tripId} " +
            "AND date = #{order.tripDay.date} ")
    @Options(useGeneratedKeys = true, keyProperty = "order.orderId")
    int insert(@Param("order") Order order);

    @Delete("DELETE FROM orders WHERE id=#{order.orderId}")
    boolean deleteOrder(@Param("order") Order order);
}

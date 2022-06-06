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
    @Insert("INSERT INTO orders (tripId, date, clientId) " +
            "VALUES (#{order.trip.tripId}, #{order.date}, #{order.client.id})")
    @Options(useGeneratedKeys = true, keyProperty = "order.orderId")
    void insert(@Param("order") Order order);

    @Select("SELECT orderId, tripId, date, clientId FROM orders WHERE clientId = #{id}")
    @Results(id = "order", value = {
            @Result(property = "trip", column = "tripId", javaType = Trip.class,
                    one = @One(select = "thumbtack.buscompany.repository.TripRepository.getTrip")),
            @Result(property = "client", column = "clientId", javaType = Client.class,
                    one = @One(select = "thumbtack.buscompany.repository.UserRepository.getUserById"))}
    )
    List<Order> getAllByClientId(@Param("id") Integer id);

    @Select("SELECT orderId, tripId, date, clientId FROM orders WHERE orderId = #{orderId}")
    @ResultMap("order")
    Order getById(Integer orderId);

    @Select("SELECT id FROM passengers WHERE passport = #{passport}")
    Integer getPassengerIdByPassport(@Param("passport") Integer passport);
}

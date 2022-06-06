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

    @Select("SELECT orderId, tripId, date, clientId FROM orders o join trips_dates td on td.id = o.trips_dates_id " +
            "WHERE clientId = #{id}")
    @Results(id = "order", value = {
            @Result(property = "trip", column = "tripId", javaType = Trip.class,
                    one = @One(select = "thumbtack.buscompany.repository.TripRepository.getTrip")),
            @Result(property = "client", column = "clientId", javaType = Client.class,
                    one = @One(select = "thumbtack.buscompany.repository.UserRepository.getUserById"))}
    )
    List<Order> getAllByClientId(@Param("id") Integer id);

    @Select("SELECT orderId, tripId, date, clientId FROM orders o join trips_dates td on td.id = o.trips_dates_id" +
            " WHERE orderId = #{orderId}")
    @ResultMap("order")
    Order getById(Integer orderId);

    @Select("SELECT id FROM passengers WHERE passport = #{passport}")
    Integer getPassengerIdByPassport(@Param("passport") Integer passport);

    @Select("SELECT id FROM trips_dates WHERE tripId=#{order.trip.tripId} AND date = #{order.date}")
    Integer getTripDateIdByOrder(@Param("") Order order);

    @Insert("INSERT INTO orders (tripDateId, clientId) " +
            "VALUE (#{tripDateId}, #{clientId})")
    @Options(useGeneratedKeys = true, keyProperty = "order.orderId")
    void insert(@Param("tripDateId") Integer tripDateId, @Param("clientId") Integer clientId);
}

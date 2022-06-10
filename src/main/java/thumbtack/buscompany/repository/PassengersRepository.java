package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;

import java.util.List;

@Mapper
@Repository
public interface PassengersRepository {
    @Insert("INSERT INTO passengers (orderId, firstName, lastName, passport) " +
            "VALUE (#{order.orderId}, #{passenger.firstName}, #{passenger.lastName}, #{passenger.passport})")
    @Options(useGeneratedKeys = true, keyProperty = "passenger.id")
    void insertPassenger(@Param("order") Order order, @Param("passenger") Passenger passenger);

    @Select("SELECT id, firstName, lastName, passport " +
            "FROM passengers " +
            "WHERE orderId = #{orderId} ")
    List<Passenger> getAllByOrderId(@Param("orderId") Integer orderId);
}

package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Passenger;

@Mapper
@Repository
public interface PassengersRepository {
    @Insert("INSERT INTO passengers (orderId, firstName, lastName, passport) " +
            "VALUE (#{order.orderId}, #{passenger.firstName}, #{passenger.lastName}, #{passenger.passport})")
    @Options(useGeneratedKeys = true, keyProperty = "passenger.id")
    void insertPassenger(@Param("order") Order order, @Param("passenger") Passenger passenger);
}

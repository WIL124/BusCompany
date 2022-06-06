package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.request.ChoosingPlaceRequest;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface PlaceRepository {

    @Select("SELECT place FROM passengers " +
            "WHERE orderId IN " +
            "(SELECT orderId from orders " +
            "WHERE tripId = #{trip.tripId} AND date = #{date})")
    List<Integer> getBookedPlaces(@Param("trip") Trip trip, @Param("date") LocalDate date);

    @Update("UPDATE passengers " +
            "SET place = #{request.place} " +
            "WHERE orderId = #{request.orderId} " +
            "AND firstName = #{request.firstName} " +
            "AND lastName = #{request.lastName} " +
            "AND passport = #{request.passport}" +
            "AND (SELECT count(place) WHERE place = #{request.place} " +
            "AND orderId = #{request.orderId}) = 0") //TODO fix me
    boolean updatePlace(@Param("request") ChoosingPlaceRequest request);
}

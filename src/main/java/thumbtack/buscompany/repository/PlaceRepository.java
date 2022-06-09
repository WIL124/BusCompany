package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.Trip;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface PlaceRepository {

    @Select("SELECT place FROM booked_places " +
            "WHERE trips_dates_id IN " +
            "(SELECT id from trips_dates " +
            "WHERE tripId = #{trip.tripId} AND date = #{date})")
    List<Integer> getBookedPlaces(@Param("trip") Trip trip, @Param("date") LocalDate date);


    @Insert("INSERT INTO booked_places (passengerId, trips_dates_id, place) " +
            "SELECT #{passenger.id}, trips_dates_id, #{place} " +
            "FROM passengers " +
            "JOIN orders o on o.orderId = passengers.orderId " +
            "JOIN trips_dates td on td.id = o.trips_dates_id " +
            "ON DUPLICATE KEY UPDATE place = #{place}")
    boolean choicePlace(@Param("passenger") Passenger passenger, @Param("place") Integer place);
}

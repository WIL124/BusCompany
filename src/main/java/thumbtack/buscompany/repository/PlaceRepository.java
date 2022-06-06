package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
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


    @Insert("INSERT INTO booked_places (passengerId, trip_date_id, place) VALUE " +
            "(#{passengerId}, #{tripDateID}, #{place}) " +
            "ON DUPLICATE KEY UPDATE place = #{place}")
    boolean choicePlace(@Param("place") Integer place, @Param("tripDateId") Integer tripDateId, @Param("passengerId") Integer passengerId);
}

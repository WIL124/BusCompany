package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Trip;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface PlaceRepository {
    @Select("SELECT placeNumber FROM booked_places " +
            "WHERE trips_dates_id IN " +
            "(SELECT id FROM trips_dates WHERE tripId = #{trip.tripId} AND date = #{date})")
    List<Integer> getBookedPlaces(@Param("trip") Trip trip, @Param("date") LocalDate date);
}

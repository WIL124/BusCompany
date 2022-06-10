package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripDay;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface TripDayRepository {
    @Insert("INSERT INTO trips_dates (tripId, date)" +
            "VALUE(#{tripDay.trip.tripId}, #{tripDay.date})")
    @Options(useGeneratedKeys = true, keyProperty = "tripDay.tripDayId")
    void insertTripDay(@Param("tripDay") TripDay tripDay);

    @Select("SELECT id as tripDayId, tripId, date " +
            "FROM trips_dates " +
            "WHERE tripDayId = #{tripDayId}") //TODO fix me trip select
    @Results(id = "tripDay", value = {
            @Result(property = "tripDayId", column = "tripDayId"),
            @Result(property = "trip", javaType = Trip.class, column = "tripId",
                    one = @One(select = "thumbtack.buscompany.repository.TripRepository.getTrip", fetchType = FetchType.LAZY)),
            @Result(property = "orders", column = "tripDayId", javaType = List.class,
                    many = @Many(select = "")) //TODO fix me
    })
    TripDay getTripDay(@Param("tripDayId") int tripDayId);

    @Delete("DELETE FROM trips_dates WHERE tripId = #{tripId}")
    boolean deleteAllTripDays(@Param("tripId") int tripId);
    @Select("SELECT id as tripDayId, tripId, date " +
            "FROM trips_dates " +
            "WHERE tripId = #{tripId} " +
            "AND date = #{date}")
    @ResultMap("tripDay")
    TripDay getTripDay(@Param("tripId") Integer tripId, @Param("date") LocalDate date);
}

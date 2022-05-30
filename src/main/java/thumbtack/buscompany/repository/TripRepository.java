package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.model.Trip;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TripRepository {
    @Insert("INSERT INTO trips (busName, fromStation, toStation, start, duration, price)" +
            " VALUES(#{trip.busName}, #{trip.fromStation}, #{trip.toStation}, #{trip.start}," +
            " #{trip.duration}, #{trip.price})")
    @Options(useGeneratedKeys = true, keyProperty = "trip.tripId")
    void insertTrip(@Param("trip") Trip trip);

    @Insert("INSERT INTO trips_dates (trip_id, date, place_count)" +
            "VALUES(#{trip.id}, #{date}, #{trip.bus.placeCount})")
    void insertTripDate(@Param("trip") Trip trip, @Param("date") LocalDate date);

    @Select("SELECT id AS tripId, fromStation, toStation, start, duration, price, approved FROM trips WHERE id = #{tripId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "bus", javaType = Bus.class, column = "busName",
                    one = @One(select = "net.thumbtack.buscompany.repository.BusRepository.get", fetchType = FetchType.EAGER)),
            @Result(property = "dates", javaType = List.class, column = "tripId",
                    many = @Many(select = "net.thumbtack.buscompany.repository.TripRepository.getTripDates", fetchType = FetchType.EAGER))
    })
    Trip getTrip(@Param("tripId") int tripId);

    @Select("SELECT date FROM trips_dates WHERE trip_id = #{tripId}")
    List<LocalDate> getTripDates(@Param("id") int tripId);

    @Update("UPDATE trips SET busName = #{trip.busName}, fromStation = #{trip.fromStation}, " +
            "toStation = #{trip.toStation}, start = #{trip.start}, duration = #{trip.duration}, " +
            "price = #{trip.price}" +
            "WHERE tripId = #{tripId}")
    boolean updateTripProperties(@Param("tripId") int tripId, @Param("trip") Trip trip);

    @Delete("DELETE * FROM trips_dates WHERE tripId = #{tripId}")
    boolean deleteAllTripDates(@Param("tripId") int tripId);

    @Delete("DELETE * FROM trips WHERE tripId = #{tripId}")
    boolean deleteTrip(@Param("tripId") int tripId);

    @Update("UPDATE trips SET approved = true WHERE tripId = #{tripId}")
    boolean approve(int tripId);
}

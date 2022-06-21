package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.model.TripDay;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TripDayRepository {
    @Insert("INSERT INTO trips_dates (tripId, date, free_places) " +
            "VALUE(#{tripDay.trip.tripId}, #{tripDay.date}, #{tripDay.trip.bus.placeCount})")
    @Options(useGeneratedKeys = true, keyProperty = "tripDay.tripDayId")
    int insertTripDay(@Param("tripDay") TripDay tripDay);

    @Select("SELECT id AS tripDayId, tripId, date " +
            "FROM trips_dates " +
            "WHERE id = #{tripDayId}")
    @Results(id = "tripDay", value = {
            @Result(property = "tripDayId", column = "tripDayId"),
            @Result(property = "date", column = "date"),
            @Result(property = "trip", javaType = Trip.class, column = "tripId",
                    one = @One(select = "thumbtack.buscompany.repository.TripRepository.getTripById", fetchType = FetchType.LAZY)),
            @Result(property = "orders", column = "tripDayId", javaType = List.class,
                    many = @Many(select = "thumbtack.buscompany.repository.OrderRepository.getByTripDayId", fetchType = FetchType.LAZY))
    })
    TripDay getTripDayById(@Param("tripDayId") int tripDayId);
    @Select("SELECT id AS tripDayId, tripId, date " +
            "FROM trips_dates " +
            "WHERE tripId = #{tripId}")
    @ResultMap("tripDay")
    List<TripDay> getTripDaysByTripId(@Param("tripId") Integer tripId);

    @Delete("DELETE FROM trips_dates WHERE tripId = #{tripId}")
    boolean deleteAllTripDays(@Param("tripId") int tripId);
    @Select("SELECT id AS tripDayId, tripId, date " +
            "FROM trips_dates " +
            "WHERE tripId = #{tripId} " +
            "AND date = #{date}")
    @ResultMap("tripDay")
    TripDay getTripDayByTripIdAndDate(@Param("tripId") Integer tripId, @Param("date") LocalDate date);

    @Update("UPDATE trips_dates SET free_places = free_places - ${order.passengers.size} " +
            "WHERE id = #{order.tripDay.tripDayId} " +
            "AND free_places >= ${order.passengers.size}")
    int reducePlaces(@Param("order") Order order);
    @Update("UPDATE trips_dates SET free_places = free_places + ${order.passengers.size} " +
            "WHERE id = #{order.tripDay.tripDayId} ")
    int increasePlaces(@Param("order") Order order);
}

package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.*;

import java.util.List;

@Mapper
public interface TripRepository {
    @Insert("INSERT INTO trips (busName, fromStation, toStation, start, duration, price)" +
            " VALUES(#{trip.bus.busName}, #{trip.fromStation}, #{trip.toStation}, #{trip.start}," +
            " #{trip.duration}, #{trip.price})")
    @Options(useGeneratedKeys = true, keyProperty = "trip.tripId")
    void insertTrip(@Param("trip") Trip trip);

    @Select("SELECT id, fromStation, busName, toStation, start, duration, price, approved " +
            "FROM trips WHERE id = #{tripId}")
    @Results(id = "trip", value = {
            @Result(property = "tripId", column = "id"),
            @Result(property = "bus", javaType = Bus.class, column = "busName",
                    one = @One(select = "thumbtack.buscompany.repository.BusRepository.get", fetchType = FetchType.LAZY)),
            @Result(property = "tripDays", javaType = List.class, column = "id",
                    many = @Many(select = "thumbtack.buscompany.repository.TripDayRepository.getTripDaysByTripId", fetchType = FetchType.LAZY))
    })
    Trip getTripById(@Param("tripId") int tripId);

    @Update("UPDATE trips SET busName = #{trip.bus.busName}, fromStation = #{trip.fromStation}, " +
            "toStation = #{trip.toStation}, start = #{trip.start}, duration = #{trip.duration}, " +
            "price = #{trip.price} " +
            "WHERE id = #{trip.tripId} " +
            "AND approved = false")
    Integer updateTripProperties(@Param("trip") Trip trip);

    @Delete("DELETE FROM trips WHERE id = #{tripId} AND approved = false")
    boolean deleteTrip(@Param("tripId") int tripId);

    @Update("UPDATE trips SET approved = true WHERE id = #{tripId}")
    boolean approve(int tripId);

    @SelectProvider(type = SqlProvider.class, method = "getTripsWithParams")
    @ResultMap("trip")
    List<Trip> getTripsWithParams(@Param("user") User user, @Param("params") RequestParams params);

    class SqlProvider {
        public static String getTripsWithParams(@Param("user") User user, @Param("params") RequestParams params) {
            return new SQL() {
                {
                    SELECT("id", "busName", "duration", "fromStation", "toStation",
                            "start", "price");
                    if (user instanceof Admin) {
                        SELECT("approved");
                    }
                    FROM("trips");
                    if (user instanceof Client) {
                        WHERE("approved = TRUE");
                    }
                    if (params != null) {
                        if (params.getBusName() != null) {
                            WHERE("busName = #{params.busName}");
                        }
                        if (params.getToStation() != null) {
                            WHERE("toStation = #{params.toStation}");
                        }
                        if (params.getFromStation() != null) {
                            WHERE("fromStation = #{params.fromStation}");
                        }
                    }
                }
            }.toString();
        }
    }
}

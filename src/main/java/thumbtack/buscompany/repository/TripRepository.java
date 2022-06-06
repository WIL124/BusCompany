package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface TripRepository {
    @Insert("INSERT INTO trips (busName, fromStation, toStation, start, duration, price)" +
            " VALUES(#{trip.busName}, #{trip.fromStation}, #{trip.toStation}, #{trip.start}," +
            " #{trip.duration}, #{trip.price})")
    @Options(useGeneratedKeys = true, keyProperty = "trip.tripId")
    void insertTrip(@Param("trip") Trip trip);

    @Insert("INSERT INTO trips_dates (trip_id, date, place_count)" +
            "VALUES(#{trip.tripId}, #{date}, #{trip.bus.placeCount})")
    void insertTripDate(@Param("trip") Trip trip, @Param("date") LocalDate date);

    @Select("SELECT tripId, fromStation, busName, toStation, start, duration, price, approved FROM trips WHERE tripId = #{tripId}")
    @Results(id = "trip", value = {
            @Result(property = "tripId", column = "tripId"),
            @Result(property = "bus", javaType = Bus.class, column = "busName",
                    one = @One(select = "thumbtack.buscompany.repository.BusRepository.get", fetchType = FetchType.DEFAULT)),
            @Result(property = "dates", javaType = List.class, column = "tripId",
                    many = @Many(select = "thumbtack.buscompany.repository.TripRepository.getTripDates", fetchType = FetchType.DEFAULT))
    })
    Trip getTrip(@Param("tripId") int tripId);

    @Select("SELECT date FROM trips_dates WHERE tripId = #{tripId}")
    List<LocalDate> getTripDates(@Param("tripId") int tripId);

    @Update("UPDATE trips SET busName = #{trip.busName}, fromStation = #{trip.fromStation}, " +
            "toStation = #{trip.toStation}, start = #{trip.start}, duration = #{trip.duration}, " +
            "price = #{trip.price}" +
            "WHERE tripId = #{tripId}" +
            "AND approved = false")
    boolean updateTripProperties(@Param("tripId") int tripId, @Param("trip") Trip trip);

    @Delete("DELETE * FROM trips_dates WHERE tripId = #{tripId}")
    boolean deleteAllTripDates(@Param("tripId") int tripId);

    @Delete("DELETE * FROM trips WHERE tripId = #{tripId} AND approved = false")
    boolean deleteTrip(@Param("tripId") int tripId);

    @Update("UPDATE trips SET approved = true WHERE tripId = #{tripId}")
    boolean approve(int tripId);

    @SelectProvider(type = SqlProvider.class, method = "getTripsWithParams")
    @ResultMap("trip")
    List<Trip> getTripsWithParams(@Param("user") User user, @Param("params") RequestParams params);

    class SqlProvider {
        public static String getTripsWithParams(User user, RequestParams params) {
            String sql = new SQL() {
                {
                    SELECT("trips.tripId AS tripId", "busName", "duration", "fromStation", "toStation",
                            "start", "price", "MAX(date) AS maxDate", "MIN(date) AS minDate");
                    if (user instanceof Admin) {
                        SELECT("approved");
                    }
                    FROM("trips");
                    LEFT_OUTER_JOIN("trips_dates ON trips.tripId = trips_dates.tripId");
                    if (user instanceof Client) {
                        WHERE("approved = TRUE");
                    }
                    if (params != null) {
                        if (params.getBusName() != null) {
                            WHERE("busName like #{params.busName}");
                        }
                        if (params.getFromDate() != null) {
                            WHERE("minDate > #{params.fromDate}");
                        }
                        if (params.getToDate() != null) {
                            WHERE("maxDate < #{params.toDate}");
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
            System.out.println(sql);
            return sql;
        }
    }
}

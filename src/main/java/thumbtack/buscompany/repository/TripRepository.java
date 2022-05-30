package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import thumbtack.buscompany.model.Trip;

import java.time.LocalDate;

@Mapper
public interface TripRepository {
    @Insert("INSERT INTO trips (busName, fromStation, toStation, start, duration, price)" +
            " VALUES(#{trip.busName}, #{trip.fromStation}, #{trip.toStation}, #{trip.start}," +
            " #{trip.duration}, #{trip.price})")
    @Options(useGeneratedKeys = true, keyProperty = "trip.id")
    void insertTrip(@Param("trip") Trip trip);

    @Insert("INSERT INTO trips_dates (trip_id, date, place_count)" +
            "VALUES(#{trip.id}, #{date}, #{trip.bus.placeCount})")
    void insertTripDate(@Param("trip") Trip trip, @Param("date") LocalDate date);
}

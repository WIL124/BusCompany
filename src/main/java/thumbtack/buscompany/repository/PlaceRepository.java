package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Passenger;
import thumbtack.buscompany.model.TripDay;

import java.util.List;

@Mapper
public interface PlaceRepository {

    @Select("SELECT place FROM booked_places " +
            "WHERE trips_dates_id = #{tripDay.tripDayId} " +
            "AND passengerId IS NULL")
    List<Integer> getFreePlaces(@Param("tripDay") TripDay tripDay);


    @Update("UPDATE booked_places " +
            "SET passengerId = #{passenger.id} " +
            "WHERE trips_dates_id = #{tripDay.tripDayId} " +
            "AND place = #{place} " +
            "AND passengerId IS NULL")
    Integer choicePlace(@Param("tripDay") TripDay tripDay, @Param("passenger") Passenger passenger, @Param("place") Integer place);

    @InsertProvider(type = SqlProvider.class, method = "insertPlaces")
    void insertPlaces(@Param("tripDay") TripDay tripDay, @Param("placeCount") Integer placeCount);

    @Update("UPDATE booked_places " +
            "SET passengerId = null " +
            "WHERE trips_dates_id = #{tripDay.tripDayId} " +
            "AND passengerId = #{passenger.id}")
    void removePassengerFromPlace(@Param("tripDay") TripDay tripDay, @Param("passenger") Passenger passenger);

    @Update("UPDATE booked_places " +
            "SET passengerId = null " +
            "WHERE passengerId = #{passenger.id}")
    boolean removePassenger(@Param("passenger") Passenger passenger);

    class SqlProvider {
        public static String insertPlaces(@Param("tripDay") TripDay tripDay, @Param("placeCount") Integer placeCount) {
            return new SQL() {
                {
                    INSERT_INTO("booked_places");
                    INTO_COLUMNS("trips_dates_id", "place");
                    for (int i = 1; i <= placeCount; i++) {
                        INTO_VALUES(Integer.toString(tripDay.getTripDayId()), Integer.toString(i));
                        ADD_ROW();
                    }
                }
            }.toString();
        }
    }
}

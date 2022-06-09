package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DebugRepository {
    @Delete("DELETE FROM sessions")
    boolean clearSessions();

    @Delete("DELETE FROM clients")
    boolean clearClients();

    @Delete("DELETE FROM admins")
    boolean clearAdmins();

    @Delete("DELETE FROM users")
    boolean clearUsers();

    @Delete("DELETE FROM trips")
    boolean clearTrips();

    @Delete("DELETE FROM trips_dates")
    boolean clearTripsDates();

    @Delete("DELETE FROM booked_places")
    boolean clearBookedPlaces();

    @Delete("DELETE FROM orders")
    boolean clearOrders();

    @Delete("DELETE FROM passengers")
    boolean clearPassengers();
}

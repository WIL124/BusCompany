package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.DebugDao;
import thumbtack.buscompany.repository.DebugRepository;

@Repository
@AllArgsConstructor
public class DebugDaoImpl implements DebugDao {
    private DebugRepository repository;
    @Override
    public void clear() {
        repository.clearBookedPlaces();
        repository.clearOrders();
        repository.clearPassengers();
        repository.clearTripsDates();
        repository.clearTrips();
        repository.clearSessions();
        repository.clearAdmins();
        repository.clearClients();
        repository.clearUsers();
    }
}

package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.DebugDao;
import thumbtack.buscompany.repository.DebugRepository;

@Repository
@AllArgsConstructor
public class DebugDaoImpl implements DebugDao {
    DebugRepository repository;
    @Override
    @Transactional
    public void clear() {
        repository.clearSessions();
        repository.clearAdmins();
        repository.clearClients();
        repository.clearUsers();
    }
}

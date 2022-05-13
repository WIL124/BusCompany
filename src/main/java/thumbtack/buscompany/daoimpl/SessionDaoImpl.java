package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.repository.SessionRepository;

@Repository
@AllArgsConstructor
public class SessionDaoImpl implements SessionDao {

    SessionRepository sessionRepository;

    @Override
    public void insert(Session session) {
        sessionRepository.insertSession(session);
    }

    @Override
    public void delete() {

    }

    @Override
    public void updateTime() {

    }

    @Override
    public Session getByUserId(Integer userId) {
        return sessionRepository.getByUserId(userId);
    }
}

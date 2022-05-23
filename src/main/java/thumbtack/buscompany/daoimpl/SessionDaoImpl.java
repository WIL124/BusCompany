package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.repository.SessionRepository;

import java.util.Date;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SessionDaoImpl implements SessionDao {

    SessionRepository sessionRepository;

    @Override
    public void insert(Session session) {
        sessionRepository.insertSession(session);
    }

    @Override
    public boolean delete(String session_id) {
        return sessionRepository.delete(session_id);
    }

    @Override
    public int adminCount() {
        return sessionRepository.adminCount();
    }

    @Override
    public boolean updateTime(String session_id) {
       return sessionRepository.updateTime(session_id, new Date().getTime());
    }

    @Override
    public Optional<Session> getBySessionById(String sessionId) {
        return Optional.ofNullable(sessionRepository.getBySessionId(sessionId));
    }
}

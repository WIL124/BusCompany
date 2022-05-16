package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.repository.SessionRepository;

import java.util.Date;

@Repository
@AllArgsConstructor
public class SessionDaoImpl implements SessionDao {

    SessionRepository sessionRepository;

    @Override
    public void insert(Session session) {
        sessionRepository.insertSession(session);
    }

    @Override
    public void delete(String session_id) {
        sessionRepository.delete(session_id);
    }

    @Override
    public int adminCount() {
        return sessionRepository.adminCount();
    }

    @Override
    public void updateTime(String session_id) {
        sessionRepository.updateTime(session_id, new Date().getTime());
    }

    @Override
    public Session getBySessionId(String session_id) {
        return sessionRepository.getBySessionId(session_id);
    }
}

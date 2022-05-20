package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Session;

import java.util.Optional;

public interface SessionDao {
    void insert(Session session);
    boolean delete(String session_id);
    boolean deleteByUserId(Integer user_id);
    int adminCount();
    boolean updateTime(String session_id);
    Optional<Session> getBySessionId(String id);
}

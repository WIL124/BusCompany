package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Session;

import java.util.Optional;

public interface SessionDao {
    void insert(Session session);
    boolean delete(String sessionId);
    int adminCount();
    boolean updateTime(String sessionId);
    Optional<Session> getSessionById(String id);
}

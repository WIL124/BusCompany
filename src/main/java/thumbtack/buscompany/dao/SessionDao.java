package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Session;

public interface SessionDao {
    void insert(Session session);
    void delete(String session_id);
    int adminCount();
    void updateTime();
    Session getBySessionId(String id);
}

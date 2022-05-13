package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Session;

public interface SessionDao {
    void insert(Session session);
    void delete(String session_id);
    void updateTime();
    Session getByUserId(Integer id);
}

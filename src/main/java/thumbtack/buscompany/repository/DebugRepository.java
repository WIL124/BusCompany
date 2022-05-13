package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DebugRepository {
    @Delete("DELETE FROM sessions")
    void clearSessions();
    @Delete("DELETE FROM clients")
    void clearClients();
    @Delete("DELETE FROM admins")
    void clearAdmins();
    @Delete("DELETE FROM users")
    void clearUsers();
}

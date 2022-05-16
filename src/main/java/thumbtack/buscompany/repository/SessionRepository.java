package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import thumbtack.buscompany.model.Session;

@Mapper
public interface SessionRepository {
    @Insert("INSERT INTO sessions VALUES (#{session.userId},#{session.sessionId},#{session.lastActivityTime}, #{session.userType})")
    int insertSession(@Param("session") Session session);

    @Select("SELECT * FROM sessions WHERE user_id = #{id}")
    Session getByUserId(@Param("id") Integer id);

    @Delete("DELETE FROM sessions WHERE session_id = #{session_id}")
    int delete(@Param("session_id") String session_id);

    @Select("SELECT * FROM sessions WHERE session_id = #{session_id}")
    Session getBySessionId(@Param("session_id") String session_id);

    @Select("SELECT COUNT(*) FROM sessions WHERE user_type = 'ADMIN'")
    int adminCount();

    @Update("UPDATE sessions SET last_activity_time = #{time} WHERE session_id = #{session_id}")
    void updateTime(@Param("session_id") String session_id, @Param("time") long time);
}

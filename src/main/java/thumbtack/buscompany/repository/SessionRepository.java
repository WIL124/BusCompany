package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import thumbtack.buscompany.model.Session;

@Mapper
public interface SessionRepository {
    @Insert("INSERT INTO sessions VALUES (#{session.userId},#{session.sessionId},#{session.lastActivityTime})")
    int insertSession(@Param("session") Session session);

    @Select("SELECT * FROM sessions WHERE user_id = #{id}")
    Session getByUserId(@Param("id") Integer id);

    @Delete("DELETE FROM sessions WHERE session_id = #{session_id}")
    int delete(@Param("session_id") String session_id);

    @Select("SELECT FROM sessions WHERE session_id = #{session_id}")
    Session getBySessionId(@Param("session_id") String session_id);
}

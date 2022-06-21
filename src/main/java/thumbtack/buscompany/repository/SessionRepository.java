package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.model.User;

@Mapper
public interface SessionRepository {
    @Insert("INSERT INTO sessions VALUES (#{session.user.id},#{session.sessionId},#{session.lastActivityTime})" +
            "ON DUPLICATE KEY UPDATE session_id= #{session.sessionId}, last_activity_time= #{session.lastActivityTime}")
    void insertSession(@Param("session") Session session);

    @Delete("DELETE FROM sessions WHERE session_id = #{sessionId}")
    boolean delete(@Param("sessionId") String sessionId);

    @Select("SELECT user_id as id, session_id AS sessionId, last_activity_time AS lastActivityTime " +
            "FROM sessions " +
            "WHERE session_id = #{sessionId}")
    @Results({
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "thumbtack.buscompany.repository.UserRepository.getUserById", fetchType = FetchType.LAZY))
    })
    Session getBySessionId(@Param("sessionId") String sessionId);

    @Select("SELECT COUNT(*) FROM admins INNER JOIN sessions s on admins.id = s.user_id")
    int adminCount();

    @Update("UPDATE sessions SET last_activity_time = #{time} WHERE session_id = #{session_id}")
    boolean updateTime(@Param("session_id") String session_id, @Param("time") long time);
}

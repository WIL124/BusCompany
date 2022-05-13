package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import thumbtack.buscompany.model.Session;

@Mapper
public interface SessionRepository {
    @Insert("INSERT INTO sessions VALUES (#{session.userId},#{session.sessionId},#{session.lastActivityTime})")
    int insertSession(@Param("session") Session session);
    @Select("SELECT * FROM sessions WHERE user_id = #{id}")
    Session getByUserId(@Param("id") Integer id);
}

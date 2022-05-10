package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.User;

@Mapper
@Repository
public interface UserRepository {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUser(@Param("id") Integer id);

    @Insert("INSERT INTO USERS (firstname, lastname, patronymic, login, password, userType)" +
            "values(#{user.firstName}, #{user.lastName}, #{user.patronymic}," +
            " #{user.login}, #{user.password}, #{user.userType})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void insertUserProperties(@Param("user") User user);

    @Insert("INSERT INTO ADMINS (id, position)" +
            "values(#{admin.id}, #{admin.position})")
    void insertAdminProperties(@Param("admin") Admin admin);

}

package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;

import java.util.List;

@Mapper
public interface UserRepository {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(@Param("id") Integer id);

    @Insert("INSERT INTO USERS (firstname, lastname, patronymic, login, password, userType)" +
            "values(#{user.firstName}, #{user.lastName}, #{user.patronymic}," +
            " #{user.login}, #{user.password}, #{user.userType})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    int insertUserProperties(@Param("user") User user);

    @Insert("INSERT INTO ADMINS (id, position)" +
            "VALUES (#{admin.id}, #{admin.position})")
    int insertAdminProperties(@Param("admin") Admin admin);

    @Insert("INSERT INTO CLIENTS (id, email, phone)" +
            "VALUES (#{client.id}, #{client.email}, #{client.phone})")
    int insertClientProperties(@Param("client") Client client);

    @Select("SELECT * FROM users WHERE login = #{login} COLLATE utf8mb4_0900_ai_ci")
    User getUserByLogin(@Param("login") String login);

    @Select("SELECT * FROM users INNER JOIN admins USING(id) WHERE id = #{id}")
    Admin getAdmin(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN clients USING(id) WHERE id = #{id}")
    Client getClient(@Param("id") Integer id);

    @Update("UPDATE users SET active = false WHERE id = #{id}")
    void deactivate(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN clients USING(id)")
    List<Client> getAllClients();
}

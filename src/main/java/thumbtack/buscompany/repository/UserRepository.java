package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;

import java.util.List;

@Mapper
public interface UserRepository {

    @Insert("INSERT INTO USERS (firstname, lastname, patronymic, login, password, userType)" +
            "values(#{user.firstName}, #{user.lastName}, #{user.patronymic}," +
            " #{user.login}, #{user.password}, #{user.userType})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void insertUserProperties(@Param("user") User user);

    @Insert("INSERT INTO ADMINS (id, position)" +
            "VALUES (#{admin.id}, #{admin.position})")
    void insertAdminProperties(@Param("admin") Admin admin);

    @Insert("INSERT INTO CLIENTS (id, email, phone)" +
            "VALUES (#{client.id}, #{client.email}, #{client.phone})")
    void insertClientProperties(@Param("client") Client client);

    @Select("SELECT * FROM users WHERE login = #{login} COLLATE utf8mb4_0900_ai_ci")
    User getUserByLogin(@Param("login") String login);

    @Select("SELECT * FROM users INNER JOIN admins USING(id) WHERE id = #{id}")
    Admin getAdminById(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN clients USING(id) WHERE id = #{id}")
    Client getClientById(@Param("id") Integer id);

    @Update("UPDATE users SET active = false WHERE id = #{id}")
    boolean deactivate(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN clients USING(id)")
    List<Client> getAllClients();

    @Update("UPDATE admins SET position = #{admin.position} WHERE id = #{admin.id}")
    boolean updateAdminProperties(@Param("admin") Admin admin);

    @Update("UPDATE users SET firstname = #{user.firstName}, " +
            "lastname = #{user.lastName}, " +
            "patronymic = #{user.patronymic}, " +
            "password = #{user.password} " +
            "WHERE id = #{user.id}")
    boolean updateUserProperties(@Param("user") User user);

    @Select("SELECT userType FROM users WHERE login = #{login}")
    UserType getUserType(@Param("login") String login);

    @Select("SELECT * FROM users INNER JOIN clients USING(id) WHERE login = #{login}")
    Client getClientByLogin(@Param("login") String login);

    @Select("SELECT * FROM users INNER JOIN admins USING(id) WHERE login = #{login}")
    Admin getAdminByLogin(@Param("login") String login);

    @Update("UPDATE clients SET email = #{client.email}, phone = #{client.phone} WHERE id = #{client.id}")
    boolean updateClientProperties(@Param("client") Client client);
}

package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserRepository {
    @Insert("INSERT INTO users (firstname, lastname, patronymic, login, password, userType)" +
            "values(#{user.firstName}, #{user.lastName}, #{user.patronymic}," +
            " #{user.login}, #{user.password}, #{user.userType})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    void insertUserProperties(@Param("user") User user);

    @Insert("INSERT INTO admins (id, position)" +
            "VALUES (#{admin.id}, #{admin.position})")
    void insertAdminProperties(@Param("admin") Admin admin);

    @Insert("INSERT INTO clients (id, email, phone)" +
            "VALUES (#{client.id}, #{client.email}, #{client.phone})")
    void insertClientProperties(@Param("client") Client client);

    @Select("SELECT U.id AS id, firstname, lastname, patronymic, login, password, " +
            "userType, position, email, phone " +
            "FROM users U " +
            "LEFT JOIN admins A on U.id = A.id " +
            "LEFT JOIN clients C on U.id = C.id " +
            "WHERE login = #{login} AND active")
    @TypeDiscriminator(column = "userType",
            cases = {
                    @Case(value = "ADMIN", type = Admin.class,
                            results = {
                                    @Result(property = "position", column = "position")}),
                    @Case(value = "CLIENT", type = Client.class,
                            results = {
                                    @Result(property = "id", column = "id"),
                                    @Result(property = "phone", column = "phone"),
                                    @Result(property = "email", column = "email"),
                                    @Result(property = "orders", column = "id", javaType = List.class
                                            , many = @Many(select = "thumbtack.buscompany.repository.OrderRepository.getAllByClientId", fetchType = FetchType.LAZY))
                            })
            })
    User getUserByLogin(@Param("login") String login);

    @Select("SELECT U.id AS id, firstname, lastname, patronymic, login, password, " +
            "userType, position, email, phone " +
            "FROM users U " +
            "LEFT JOIN admins A on U.id = A.id " +
            "LEFT JOIN clients C on U.id = C.id " +
            "WHERE U.id = #{id} " +
            "AND active = 1")
    @TypeDiscriminator(column = "userType",
            cases = {
                    @Case(value = "ADMIN", type = Admin.class,
                            results = {
                                    @Result(property = "position", column = "position")}),
                    @Case(value = "CLIENT", type = Client.class,
                            results = {
                                    @Result(property = "id", column = "id"),
                                    @Result(property = "phone", column = "phone"),
                                    @Result(property = "email", column = "email"),
                                    @Result(property = "orders", column = "id", javaType = List.class
                                            , many = @Many(select = "thumbtack.buscompany.repository.OrderRepository.getAllByClientId", fetchType = FetchType.LAZY))
                            })
            })
    User getUserById(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN admins USING(id) WHERE id = #{id}")
    Admin getAdminById(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN clients USING(id) WHERE id = #{id}")
    @Results(id = "client", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "orders", column = "id", javaType = List.class
                    , many = @Many(select = "thumbtack.buscompany.repository.OrderRepository.getAllByClientId", fetchType = FetchType.LAZY))
    })
    Client getClientById(@Param("id") Integer id);

    @Update("UPDATE users SET active = false WHERE id = #{id}")
    boolean deactivate(@Param("id") Integer id);

    @Select("SELECT * FROM users INNER JOIN clients USING(id)")
    @ResultMap("client")
    List<Client> getAllClients();

    @Update("UPDATE admins SET position = #{admin.position} WHERE id = #{admin.id}")
    boolean updateAdminProperties(@Param("admin") Admin admin);

    @Update("UPDATE users SET firstname = #{user.firstName}, " +
            "lastname = #{user.lastName}, " +
            "patronymic = #{user.patronymic}, " +
            "password = #{user.password} " +
            "WHERE id = #{user.id}")
    boolean updateUserProperties(@Param("user") User user);

    @Update("UPDATE clients SET email = #{client.email}, phone = #{client.phone} WHERE id = #{client.id}")
    boolean updateClientProperties(@Param("client") Client client);


    @Select("SELECT users.id AS id, firstname, lastname, patronymic, login, password, userType " +
            "FROM users " +
            "LEFT JOIN admins a on users.id = a.id " +
            "LEFT JOIN clients c on users.id = c.id " +
            "JOIN sessions s on users.id = s.user_id " +
            "WHERE session_id = #{sessionId} " +
            "AND active = 1")
    @TypeDiscriminator(column = "userType",
            cases = {
                    @Case(value = "ADMIN", type = Admin.class,
                            results = {
                                    @Result(property = "position", column = "position")}),
                    @Case(value = "CLIENT", type = Client.class,
                            results = {
                                    @Result(property = "id", column = "id"),
                                    @Result(property = "phone", column = "phone"),
                                    @Result(property = "email", column = "email"),
                                    @Result(property = "orders", column = "id", javaType = List.class
                                            , many = @Many(select = "thumbtack.buscompany.repository.OrderRepository.getAllByClientId", fetchType = FetchType.LAZY))
                            })
            })
    User getUserBySessionId(@Param("sessionId") String sessionId);

    @Select("SELECT users.id AS id, firstname, lastname, patronymic, login, password, userType " +
            "FROM users " +
            "JOIN admins a on users.id = a.id " +
            "JOIN sessions s on users.id = s.user_id " +
            "WHERE session_id = #{sessionId} " +
            "AND active = 1")
    Admin getAdminBySessionId(String sessionId);
}

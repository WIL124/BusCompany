package thumbtack.buscompany.dao;

import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void insert(Admin admin);

    void insert(Client client);

    Optional<? extends User> getUserByLogin(String login) throws ServerException;

    List<Client> getAllClients();

    boolean updateAdmin(Admin admin);

    boolean updateClient(Client client);

    User getUserById(int id);
}

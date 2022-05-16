package thumbtack.buscompany.dao;

import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;

public interface UserDao {
    void insert(Admin admin);

    void insert(Client client);

    User getUserById(Integer id) throws ServerException;

    User getUserByLogin(String login);

    Admin getAdminById(Integer id);

    Client getClientById(Integer id);
}

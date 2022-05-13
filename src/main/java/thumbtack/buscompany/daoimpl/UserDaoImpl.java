package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.repository.UserRepository;

@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    UserRepository userRepository;

    @Override
    public void insert(Admin admin) {
        userRepository.insertUserProperties(admin);
        userRepository.insertAdminProperties(admin);
    }

    @Override
    public void insert(Client client) {
        userRepository.insertUserProperties(client);
        userRepository.insertClientProperties(client);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getUserById(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return userRepository.getAdmin(id);
    }

    @Override
    public Client getClientById(Integer id) {
        return userRepository.getClient(id);
    }
}

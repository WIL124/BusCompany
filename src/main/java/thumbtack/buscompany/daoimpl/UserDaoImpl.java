package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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
    public Optional<? extends User> getUserByLogin(String login) {
        return Optional.ofNullable(userRepository.getUserByLogin(login));
    }

    @Override
    public List<Client> getAllClients() {
        return userRepository.getAllClients();
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        return (userRepository.updateUserProperties(admin) && userRepository.updateAdminProperties(admin));
    }

    @Override
    public boolean updateClient(Client client) {
        return (userRepository.updateUserProperties(client) && userRepository.updateClientProperties(client));
    }

    @Override
    public Optional<User> getBySessionId(String sessionId) {
        return Optional.ofNullable(userRepository.getUserBySessionId(sessionId));
    }

    @Override
    public Optional<Admin> getAdminBySessionId(String sessionId) {
        return Optional.ofNullable(userRepository.getAdminBySessionId(sessionId));
    }
}

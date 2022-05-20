package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;
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
    public Optional<UserType> getUserType(String login) {
        return Optional.ofNullable(userRepository.getUserType(login));
    }

    @Override
    public Optional<? extends User> getUserByLogin(String login) {
        UserType userType = getUserType(login).orElse(null);
        if (userType == UserType.ADMIN) {
            return getAdminByLogin(login);
        } else if (userType == UserType.CLIENT) {
            return getClientByLogin(login);
        } else return Optional.empty();
    }

    @Override
    public Admin getAdminById(Integer id) {
        return userRepository.getAdminById(id);
    }

    @Override
    public Client getClientById(Integer id) {
        return userRepository.getClientById(id);
    }

    public Optional<Client> getClientByLogin(String login) {
        return Optional.ofNullable(userRepository.getClientByLogin(login));
    }

    public Optional<Admin> getAdminByLogin(String login) {
        return Optional.ofNullable(userRepository.getAdminByLogin(login));
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
}

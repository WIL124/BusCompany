package thumbtack.buscompany.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import thumbtack.buscompany.BuscompanyApplicationTests;
import thumbtack.buscompany.dao.DebugDao;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static thumbtack.buscompany.TestUtils.createAdmin;
import static thumbtack.buscompany.TestUtils.createClient;

public class UserRepositoryTest extends BuscompanyApplicationTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    DebugDao debugDao;

    @BeforeEach
    public void clear() {
        debugDao.clear();
    }

    @Test
    public void insertAndGetAdmin() {
        Admin insertedUser = createAdmin();
        userRepository.insertUserProperties(insertedUser);
        userRepository.insertAdminProperties(insertedUser);
        User user = userRepository.getUserById(insertedUser.getId());
        assertEquals(user, insertedUser);
    }

    @Test
    public void insertUserProperties_shouldReturnId() {
        User user = createClient();
        userRepository.insertUserProperties(user);
        assertNotEquals(0, user.getId());
    }

    @Test
    public void insertClientProperties_shouldReturnId() {
        Client client = createClient();
        userRepository.insertUserProperties(client);
        userRepository.insertClientProperties(client);
        assertNotEquals(0, client.getId());
    }

    @Test
    public void insertAdminProperties_shouldReturnId() {
        Admin admin = createAdmin();
        userRepository.insertUserProperties(admin);
        userRepository.insertAdminProperties(admin);
        assertNotEquals(0, admin.getId());
    }

    @Test
    public void selectAdmin() {
        Admin admin = createAdmin();
        userRepository.insertUserProperties(admin);
        userRepository.insertAdminProperties(admin);
        assertNotEquals(0, admin.getId());
        assertEquals(admin, userRepository.getUserById(admin.getId()));
    }

    @Test
    public void selectClient() {
        Client client = createClient();
        userRepository.insertUserProperties(client);
        userRepository.insertClientProperties(client);
        assertNotEquals(0, client.getId());
        assertEquals(client, userRepository.getUserById(client.getId()));
    }

    @Test
    public void getAdminCount() {
        sessionRepository.adminCount();
    }

    @Test
    public void deleteAdmin() {
        sessionRepository.delete("79d52190-31bb-4661-b288-b768cdf97504");
    }
}

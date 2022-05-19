package thumbtack.buscompany.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import thumbtack.buscompany.dao.DebugDao;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static thumbtack.buscompany.TestUtils.createAdmin;
import static thumbtack.buscompany.TestUtils.createClient;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    DebugDao debugDao;

    @Before
    public void clear() {
        debugDao.clear();
    }

    @Test
    public void insertAndGetAdmin() {
        Admin insertedUser = createAdmin();
        userRepository.insertUserProperties(insertedUser);
        userRepository.insertAdminProperties(insertedUser);
        User user = userRepository.getAdminById(insertedUser.getId());
        assertThat(user).isEqualTo(insertedUser);
    }

    @Test
    public void insertUserProperties_shouldReturnId() {
        User user = createClient();
        userRepository.insertUserProperties(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void insertClientProperties_shouldReturnId() {
        Client client = createClient();
        userRepository.insertUserProperties(client);
        userRepository.insertClientProperties(client);
        assertThat(client.getId()).isNotNull();
    }

    @Test
    public void insertAdminProperties_shouldReturnId() {
        Admin admin = createAdmin();
        userRepository.insertUserProperties(admin);
        userRepository.insertAdminProperties(admin);
        assertThat(admin.getId()).isNotNull();
    }

    @Test
    public void selectAdmin() {
        Admin admin = createAdmin();
        userRepository.insertUserProperties(admin);
        userRepository.insertAdminProperties(admin);
        assertThat(admin.getId()).isNotNull();
        assertThat(userRepository.getAdminById(admin.getId())).isEqualTo(admin);
    }

    @Test
    public void selectClient() {
        Client client = createClient();
        userRepository.insertUserProperties(client);
        userRepository.insertClientProperties(client);
        assertThat(client.getId()).isNotNull();
        assertThat(userRepository.getClientById(client.getId())).isEqualTo(client);
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

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
import static thumbtack.buscompany.TestUtils.*;


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
    public void insertAndGetUser() {
        User insertedUser = createUser();
        userRepository.insertUserProperties(insertedUser);
        User user = userRepository.getUserById(insertedUser.getId());
        assertThat(user).isEqualTo(insertedUser);
    }

    @Test
    public void insertUserProperties_shouldReturn1() {
        User user = createUser();
        assertThat(userRepository.insertUserProperties(user)).isEqualTo(1);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void insertClientProperties_shouldReturn1() {
        Client client = createClient();
        assertThat(userRepository.insertUserProperties(client)).isEqualTo(1);
        assertThat(userRepository.insertClientProperties(client)).isEqualTo(1);
        assertThat(client.getId()).isNotNull();
    }

    @Test
    public void insertAdminProperties_shouldReturn1() {
        Admin admin = createAdmin();
        assertThat(userRepository.insertUserProperties(admin)).isEqualTo(1);
        assertThat(userRepository.insertAdminProperties(admin)).isEqualTo(1);
        assertThat(admin.getId()).isNotNull();
    }

    @Test
    public void selectAdmin() {
        Admin admin = createAdmin();
        assertThat(userRepository.insertUserProperties(admin)).isEqualTo(1);
        assertThat(userRepository.insertAdminProperties(admin)).isEqualTo(1);
        assertThat(admin.getId()).isNotNull();
        assertThat(userRepository.getAdmin(admin.getId())).isEqualTo(admin);
    }

    @Test
    public void selectClient() {
        Client client = createClient();
        assertThat(userRepository.insertUserProperties(client)).isEqualTo(1);
        assertThat(userRepository.insertClientProperties(client)).isEqualTo(1);
        assertThat(client.getId()).isNotNull();
        assertThat(userRepository.getClient(client.getId())).isEqualTo(client);
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
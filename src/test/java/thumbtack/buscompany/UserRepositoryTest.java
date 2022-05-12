package thumbtack.buscompany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static thumbtack.buscompany.TestUtils.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void whenRecordsInDatabase_shouldReturnUserWithGivenId() {
        User user = userRepository.getUserById(1);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getFirstName()).isEqualTo("Владислав");
        assertThat(user.getLastName()).isEqualTo("Инютин");
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
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
}

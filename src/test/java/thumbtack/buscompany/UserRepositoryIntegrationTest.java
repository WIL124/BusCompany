package thumbtack.buscompany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import thumbtack.buscompany.repository.UserRepository;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void whenRecordsInDatabase_shouldReturnUserWithGivenId() {
        User user = userRepository.getUser(1);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getFirstName()).isEqualTo("Владислав");
        assertThat(user.getLastName()).isEqualTo("Инютин");
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
    }
}

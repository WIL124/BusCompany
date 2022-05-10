package thumbtack.buscompany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void successAdminRegistration() {
        AdminRegisterRequest requestBody = new AdminRegisterRequest("Владислав", "Инютин",
                null, "admin", "goodLogin", "goodPassword");
        ResponseEntity<AdminRegisterResponse> response = restTemplate
                .postForEntity("/api/admins", requestBody, AdminRegisterResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AdminRegisterResponse body = response.getBody();
        assert body != null;
        assertThat(body.getId()).isNotNull();
        assertThat(body.getFirstName()).isEqualTo(requestBody.getFirstName());
        assertThat(body.getLastName()).isEqualTo(requestBody.getLastName());
        assertThat(body.getPatronymic()).isEqualTo(requestBody.getPatronymic());
        assertThat(body.getPosition()).isEqualTo(requestBody.getPosition());
        assertThat(body.getUserType()).isEqualTo(UserType.ADMIN);
    }
}

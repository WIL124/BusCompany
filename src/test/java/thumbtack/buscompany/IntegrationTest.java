package thumbtack.buscompany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import thumbtack.buscompany.request.AdminRequest;
import thumbtack.buscompany.response.AdminResponse;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testName() {
        ResponseEntity<AdminResponse> response = restTemplate.postForEntity("/api/admins",
                new AdminRequest("Владислав", "Инютин", null, "admin", "goodLogin", "goodPassword"),
                AdminResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

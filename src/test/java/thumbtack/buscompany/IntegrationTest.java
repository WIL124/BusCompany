package thumbtack.buscompany;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;
import thumbtack.buscompany.response.ClientRegisterResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static thumbtack.buscompany.TestUtils.createAdminRegReq;
import static thumbtack.buscompany.TestUtils.createClientRegReq;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void successAdminRegistration() {
        AdminRegisterRequest requestBody = createAdminRegReq();
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
        HttpHeaders headers = response.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        assertThat(set_cookie).contains("JAVASESSIONID=");
    }
    @Test
    public void successClientRegistration() {
        ClientRegisterRequest requestBody = createClientRegReq();
        ResponseEntity<ClientRegisterResponse> response = restTemplate
                .postForEntity("/api/clients", requestBody, ClientRegisterResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ClientRegisterResponse body = response.getBody();
        assert body != null;
        assertThat(body.getId()).isNotNull();
        assertThat(body.getFirstName()).isEqualTo(requestBody.getFirstName());
        assertThat(body.getLastName()).isEqualTo(requestBody.getLastName());
        assertThat(body.getPatronymic()).isEqualTo(requestBody.getPatronymic());
        assertThat(body.getEmail()).isEqualTo(requestBody.getEmail());
        assertThat(body.getPhone()).isEqualTo(requestBody.getPhone().replaceAll("-", ""));
        assertThat(body.getUserType()).isEqualTo(UserType.CLIENT);
        HttpHeaders headers = response.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        assertThat(set_cookie).contains("JAVASESSIONID=");
    }
}

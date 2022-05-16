package thumbtack.buscompany;

import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.ResponseBody;
import thumbtack.buscompany.dao.DebugDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.Errors;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;
import thumbtack.buscompany.response.ClientRegisterResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static thumbtack.buscompany.TestUtils.createAdminRegReq;
import static thumbtack.buscompany.TestUtils.createClientRegReq;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@NoArgsConstructor
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    DebugDao debugDao;

    @Before
    public void clear() {
        debugDao.clear();
    }

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

    @Test
    public void twiceRegisterShouldBeHandled() {
        AdminRegisterRequest requestBody = createAdminRegReq();
        ResponseEntity<AdminRegisterResponse> goodResponse = restTemplate
                .postForEntity("/api/admins", requestBody, AdminRegisterResponse.class);
        assertThat(goodResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Errors> badResponse = restTemplate
                .postForEntity("/api/admins", requestBody, Errors.class);
        assertThat(badResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Errors errors = badResponse.getBody();
        assert errors != null;
        assertThat(errors.getErrors().size()).isOne();
        ApiErrors apiErrors = errors.getErrors().get(0);
        assertThat(apiErrors.getErrorCode()).isEqualTo(ErrorCode.LOGIN_ALREADY_EXISTS.toString());
        assertThat(apiErrors.getField()).isEqualTo("login");
    }

    @Test
    public void adminLogout() {
        AdminRegisterRequest firstAdmin = createAdminRegReq();
        ResponseEntity<AdminRegisterResponse> response1 = restTemplate
                .postForEntity("/api/admins", firstAdmin, AdminRegisterResponse.class);
        String session1 = getSessionId(response1);
        AdminRegisterRequest secondAdmin = createAdminRegReq();
        secondAdmin.setLogin("anotherLogin");
        ResponseEntity<AdminRegisterResponse> response2 = restTemplate
                .postForEntity("/api/admins", secondAdmin, AdminRegisterResponse.class);
        String session2 = getSessionId(response2);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, session1);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        assertThat(restTemplate.exchange("/api/sessions", HttpMethod.DELETE, entity, Void.class)
                .getStatusCode().value()).isEqualTo(200);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add(HttpHeaders.COOKIE, session2);
        HttpEntity<String> entity2 = new HttpEntity<>(headers2);
        ResponseEntity<Errors> errorsResponseEntity = restTemplate.exchange("/api/sessions", HttpMethod.DELETE, entity2, Errors.class);
        assertThat(errorsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        List<ApiErrors> errors = errorsResponseEntity.getBody().getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo(new ApiErrors("ONE_ACTIVE_ADMIN", "JAVASESSIONID", "At least one admin must be online"));
    }

    private String getSessionId(ResponseEntity<?> response) {
        return response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
    }
}

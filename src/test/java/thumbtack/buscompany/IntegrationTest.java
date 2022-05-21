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
import thumbtack.buscompany.dao.DebugDao;
import thumbtack.buscompany.exception.ApiErrors;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.Errors;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.ClientResponse;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void successAdminRegistrationAndLogin() {
        AdminRegisterRequest requestBody = createAdminRegReq();
        ResponseEntity<AdminResponse> response = restTemplate
                .postForEntity("/api/admins", requestBody, AdminResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AdminResponse body = response.getBody();
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
    public void successClientRegistrationAndLogin() {
        ClientRegisterRequest requestBody = createClientRegReq();
        ResponseEntity<ClientResponse> response = restTemplate
                .postForEntity("/api/clients", requestBody, ClientResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ClientResponse body = response.getBody();
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
        ResponseEntity<AdminResponse> goodResponse = restTemplate
                .postForEntity("/api/admins", requestBody, AdminResponse.class);
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
        String session1 = registerAdminAndGetSessionId("firstAdmin");
        String session2 = registerAdminAndGetSessionId("secondAdmin");
        HttpEntity<Object> entity = entityWithSessionId(null, session1);
        assertThat(restTemplate.exchange("/api/sessions", HttpMethod.DELETE, entity, Void.class)
                .getStatusCode().value()).isEqualTo(200);
        HttpEntity<Object> entity2 = entityWithSessionId(null, session2);
        ResponseEntity<Errors> errorsResponseEntity = restTemplate.exchange("/api/sessions", HttpMethod.DELETE, entity2, Errors.class);
        assertThat(errorsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        List<ApiErrors> errors = Objects.requireNonNull(errorsResponseEntity.getBody()).getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo(new ApiErrors("ONE_ACTIVE_ADMIN", "JAVASESSIONID", "At least one admin must be online"));
    }

    @Test
    public void deleteUserAccount() {
        String session = registerClientAndGetSessionId("clientLogin");
        HttpEntity<Object> entity = entityWithSessionId(null, session);
        ResponseEntity<Errors> response = (restTemplate.exchange("/api/accounts", HttpMethod.DELETE, entity, Errors.class));
        assertThat(response.getStatusCode().value()).isEqualTo(200);

        //try to register again
        ClientRegisterRequest request = createClientRegReq();
        ResponseEntity<Errors> regErrors = restTemplate.postForEntity("/api/clients", request, Errors.class);
        assertThat(regErrors.getStatusCode().value()).isEqualTo(400);
        assertThat(regErrors.getBody().getErrors().get(0).getErrorCode()).isEqualTo("LOGIN_ALREADY_EXISTS");

        //try to log in
        LoginRequest loginRequest = new LoginRequest("clientLogin", "goodPassword");
        ResponseEntity<Errors> loginErrors = restTemplate.postForEntity("/api/sessions", loginRequest, Errors.class);
        assertThat(loginErrors.getStatusCodeValue()).isEqualTo(400);
        assertThat(loginErrors.getBody().getErrors().get(0).getErrorCode()).isEqualTo("DELETED_USER");
    }

    @Test
    public void getAccountInfoAdmin() {
        AdminRegisterRequest adminRegReq = createAdminRegReq();
        ResponseEntity<AdminResponse> regResponse = restTemplate
                .postForEntity("/api/admins", adminRegReq, AdminResponse.class);
        String session = getSessionId(regResponse);

        HttpEntity<Object> entity = entityWithSessionId(null, session);
        ResponseEntity<AdminResponse> accountInfoResponse = (restTemplate.exchange("/api/accounts", HttpMethod.GET, entity, AdminResponse.class));
        assertThat(accountInfoResponse.getStatusCodeValue()).isEqualTo(200);
        assertEquals(regResponse.getBody(), accountInfoResponse.getBody());
    }

    @Test
    public void getAccountInfoClient() {
        ClientRegisterRequest clientRegReq = createClientRegReq();
        ResponseEntity<ClientResponse> regResponse = restTemplate
                .postForEntity("/api/clients", clientRegReq, ClientResponse.class);
        String session = getSessionId(regResponse);

        HttpEntity<Object> entity = entityWithSessionId(null, session);
        ResponseEntity<ClientResponse> accountInfoResponse = (restTemplate.exchange("/api/accounts", HttpMethod.GET, entity, ClientResponse.class));
        assertThat(accountInfoResponse.getStatusCodeValue()).isEqualTo(200);
        assertEquals(regResponse.getBody(), accountInfoResponse.getBody());
    }

    @Test
    public void getAccountInfo_sessionNotFound() {
        HttpEntity<Object> entity = entityWithSessionId(null, "JAVASESSIONID=nonExistentSession");
        ResponseEntity<Errors> errorsResponse = (restTemplate.exchange("/api/accounts", HttpMethod.GET, entity, Errors.class));
        assertThat(errorsResponse.getStatusCodeValue()).isEqualTo(400);
        assertThat(errorsResponse.getBody().getErrors().size()).isEqualTo(1);
        assertThat(errorsResponse.getBody().getErrors().get(0).getMessage()).isEqualTo(ErrorCode.SESSION_NOT_FOUND.getMessage());

    }

    @Test
    public void updateAdmin() {
        String session = registerAdminAndGetSessionId("goodAdmin");
        String position = "НоваяДолжность";
        String firstname = "НовоеИмя";
        String lastname = "НоваяФамилия";
        String patronymic = "НовоеОтчество";
        String newPass = "НовыйПароль";
        AdminUpdateRequest body = new AdminUpdateRequest(firstname, lastname,
                patronymic, position, newPass, "goodPassword");
        HttpEntity<Object> entity = entityWithSessionId(body, session);
        ResponseEntity<AdminResponse> response =
                restTemplate.exchange("/api/admins", HttpMethod.PUT, entity, AdminResponse.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        AdminResponse responseBody = response.getBody();
        assertThat(responseBody.getPosition()).isEqualTo(position);
        assertThat(responseBody.getFirstName()).isEqualTo(firstname);
        assertThat(responseBody.getLastName()).isEqualTo(lastname);
    }

    private String getSessionId(ResponseEntity<?> response) {
        return Objects.requireNonNull(response.getHeaders().get(HttpHeaders.SET_COOKIE)).get(0);
    }

    private String registerAdminAndGetSessionId(String login) {
        AdminRegisterRequest adminRegReq = createAdminRegReq();
        adminRegReq.setLogin(login);
        ResponseEntity<AdminResponse> response = restTemplate
                .postForEntity("/api/admins", adminRegReq, AdminResponse.class);
        return getSessionId(response);
    }

    private String registerClientAndGetSessionId(String login) {
        ClientRegisterRequest clientRegReq = createClientRegReq();
        clientRegReq.setLogin(login);
        ResponseEntity<ClientResponse> response = restTemplate
                .postForEntity("/api/clients", clientRegReq, ClientResponse.class);
        return getSessionId(response);
    }

    private HttpEntity<Object> entityWithSessionId(Object body, String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionId);
        return new HttpEntity<>(body, headers);
    }
}

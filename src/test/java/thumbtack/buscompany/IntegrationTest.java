package thumbtack.buscompany;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

import static org.junit.jupiter.api.Assertions.*;
import static thumbtack.buscompany.TestUtils.createAdminRegReq;
import static thumbtack.buscompany.TestUtils.createClientRegReq;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@NoArgsConstructor
public class IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void clear() {
        restTemplate.postForLocation("/api/debug/clear", null);
    }

    @Test
    public void successAdminRegistrationAndLogin() {
        AdminRegisterRequest requestBody = createAdminRegReq();
        ResponseEntity<AdminResponse> response = restTemplate
                .postForEntity("/api/admins", requestBody, AdminResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        AdminResponse body = response.getBody();
        assert body != null;
        assertNotNull(body.getId());
        assertAll("body",
                () -> assertEquals(body.getFirstName(), requestBody.getFirstName()),
                () -> assertEquals(body.getLastName(), requestBody.getLastName()),
                () -> assertEquals(body.getPatronymic(), requestBody.getPatronymic()),
                () -> assertEquals(body.getPosition(), requestBody.getPosition()),
                () -> assertEquals(body.getUserType(), UserType.ADMIN)
        );
        HttpHeaders headers = response.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        assert set_cookie != null;
        assertTrue(set_cookie.contains("JAVASESSIONID="));
    }

    @Test
    public void successClientRegistrationAndLogin() {
        ClientRegisterRequest requestBody = createClientRegReq();
        ResponseEntity<ClientResponse> response = restTemplate
                .postForEntity("/api/clients", requestBody, ClientResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        ClientResponse body = response.getBody();
        assert body != null;
        assertNotNull(body.getId());
        assertAll("body",
                () -> assertEquals(body.getFirstName(), requestBody.getFirstName()),
                () -> assertEquals(body.getLastName(), requestBody.getLastName()),
                () -> assertEquals(body.getPatronymic(), requestBody.getPatronymic()),
                () -> assertEquals(body.getEmail(), requestBody.getEmail()),
                () -> assertEquals(body.getPhone(), requestBody.getPhone().replaceAll("-", "")),
                () -> assertEquals(body.getUserType(), UserType.CLIENT)
        );
        HttpHeaders headers = response.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        assert set_cookie != null;
        assertTrue(set_cookie.contains("JAVASESSIONID="));
    }

    @Test
    public void twiceRegisterShouldBeHandled() {
        AdminRegisterRequest requestBody = createAdminRegReq();
        ResponseEntity<AdminResponse> goodResponse = restTemplate
                .postForEntity("/api/admins", requestBody, AdminResponse.class);
        assertEquals(goodResponse.getStatusCode(), HttpStatus.OK);
        ResponseEntity<Errors> badResponse = restTemplate
                .postForEntity("/api/admins", requestBody, Errors.class);
        assertEquals(badResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        Errors errors = badResponse.getBody();
        assert errors != null;
        assertEquals(errors.getErrors().size(), 1);
        ApiErrors apiErrors = errors.getErrors().get(0);
        assertEquals(apiErrors.getErrorCode(), ErrorCode.LOGIN_ALREADY_EXISTS.toString());
        assertEquals(apiErrors.getField(), "login");
    }

    @Test
    public void adminLogout() {
        String session1 = registerAdminAndGetSessionId("firstAdmin");
        String session2 = registerAdminAndGetSessionId("secondAdmin");
        HttpEntity<Object> entity = entityWithSessionId(null, session1);
        assertEquals(restTemplate.exchange("/api/sessions", HttpMethod.DELETE, entity, Void.class)
                .getStatusCodeValue(), 200);
        HttpEntity<Object> entity2 = entityWithSessionId(null, session2);
        ResponseEntity<Errors> errorsResponseEntity = restTemplate.exchange("/api/sessions", HttpMethod.DELETE, entity2, Errors.class);
        assertEquals(errorsResponseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
        List<ApiErrors> errors = Objects.requireNonNull(errorsResponseEntity.getBody()).getErrors();
        assertEquals(errors.size(), 1);
        assertEquals(errors.get(0), new ApiErrors("ONE_ACTIVE_ADMIN", "JAVASESSIONID", "At least one admin must be online"));
    }

    @Test
    public void deleteUserAccount() {
        String session = registerClientAndGetSessionId("clientLogin");
        HttpEntity<Object> entity = entityWithSessionId(null, session);
        ResponseEntity<Errors> response = (restTemplate.exchange("/api/accounts", HttpMethod.DELETE, entity, Errors.class));
        assertEquals(response.getStatusCodeValue(), 200);

        //try to register again
        ClientRegisterRequest request = createClientRegReq();
        ResponseEntity<Errors> regErrors = restTemplate.postForEntity("/api/clients", request, Errors.class);
        assertEquals(regErrors.getStatusCode().value(), 400);
        assertEquals(Objects.requireNonNull(regErrors.getBody()).getErrors().get(0).getErrorCode(), "LOGIN_ALREADY_EXISTS");

        //try to log in
        LoginRequest loginRequest = new LoginRequest("clientLogin", "goodPassword");
        ResponseEntity<Errors> loginErrors = restTemplate.postForEntity("/api/sessions", loginRequest, Errors.class);
        assertEquals(loginErrors.getStatusCodeValue(), 400);
        assertEquals(Objects.requireNonNull(loginErrors.getBody()).getErrors().get(0).getErrorCode(), "USER_NOT_FOUND");
    }

    @Test
    public void getAccountInfoAdmin() {
        AdminRegisterRequest adminRegReq = createAdminRegReq();
        ResponseEntity<AdminResponse> regResponse = restTemplate
                .postForEntity("/api/admins", adminRegReq, AdminResponse.class);
        String session = getSessionId(regResponse);

        HttpEntity<Object> entity = entityWithSessionId(null, session);
        ResponseEntity<AdminResponse> accountInfoResponse = (restTemplate.exchange("/api/accounts", HttpMethod.GET, entity, AdminResponse.class));
        assertEquals(accountInfoResponse.getStatusCodeValue(), 200);
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
        assertEquals(accountInfoResponse.getStatusCodeValue(), 200);
        assertEquals(regResponse.getBody(), accountInfoResponse.getBody());
    }

    @Test
    public void getAccountInfo_sessionNotFound() {
        HttpEntity<Object> entity = entityWithSessionId(null, "JAVASESSIONID=nonExistentSession");
        ResponseEntity<Errors> errorsResponse = (restTemplate.exchange("/api/accounts", HttpMethod.GET, entity, Errors.class));
        assertEquals(errorsResponse.getStatusCodeValue(), 400);
        assertEquals(Objects.requireNonNull(errorsResponse.getBody()).getErrors().size(), 1);
        assertEquals(errorsResponse.getBody().getErrors().get(0).getMessage(), ErrorCode.SESSION_NOT_FOUND.getMessage());
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
        assertEquals(response.getStatusCodeValue(), 200);
        AdminResponse responseBody = response.getBody();
        assert responseBody != null;
        assertEquals(responseBody.getPosition(), position);
        assertEquals(responseBody.getFirstName(), firstname);
        assertEquals(responseBody.getLastName(), lastname);
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

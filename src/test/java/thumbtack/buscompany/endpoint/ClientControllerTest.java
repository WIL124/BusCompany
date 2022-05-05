package thumbtack.buscompany.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.Errors;
import thumbtack.buscompany.service.ClientService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerTest extends RestControllerTest {
    private static final String URL = "/api/clients";
    @MockBean
    ClientService clientService;

    @Before
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void clientRegister_ShouldCallClientService() throws Exception {
        ClientRegisterRequest clientRegisterRequest = getClientRegisterRequest();
        postRequestWithBody(URL, clientRegisterRequest).andExpect(status().isOk());
        verify(clientService).register(clientRegisterRequest);
    }
    public static Stream<Arguments> invalidPhoneNumbers() {
        return Stream.of(
                Arguments.arguments("12345678901"),
                Arguments.arguments("123456"),
                Arguments.arguments("79087961203"),
                Arguments.arguments("+89087961203"),
                Arguments.arguments("+790879612031"),
                Arguments.arguments("+7908796120"),
                Arguments.arguments("8908796120"),
                Arguments.arguments("890879612031"),
                Arguments.arguments("690879612031"),
                Arguments.arguments("-79087961203"),
                Arguments.arguments("-+79087961203"),
                Arguments.arguments("+79087961203-"),
                Arguments.arguments("+7(908)7961203")
        );
    }
    @ParameterizedTest
    @MethodSource("invalidPhoneNumbers")
    public void registerClient_invalidPhone_shouldReturn400AndMessage(String phoneNumber) throws Exception { //Testing Phone annotation
        ClientRegisterRequest clientRegisterRequest = getClientRegisterRequest();
        clientRegisterRequest.setPhone(phoneNumber);
        MvcResult result = postRequestWithBody(URL, clientRegisterRequest)
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        assertTrue(result.getResponse().getContentAsString().contains("field\":\"phone\""));
        assertEquals(1, mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().size());
        assertEquals("incorrect phone number format", mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().stream().findFirst().get().getMessage());
    }
    public static Stream<Arguments> validPhoneNumbers() {
        return Stream.of(
                Arguments.arguments("89087961203"),
                Arguments.arguments("8-908-796-12-03"),
                Arguments.arguments("89-08-7-9-612-0-3"),
                Arguments.arguments("+7-908-796-12-03"),
                Arguments.arguments("+79087961203")
        );
    }
    @ParameterizedTest
    @MethodSource("validPhoneNumbers")
    public void registerClient_validPhone_shouldCallService(String phoneNumber) throws Exception { //Testing Phone annotation
        ClientRegisterRequest clientRegisterRequest = getClientRegisterRequest();
        clientRegisterRequest.setPhone(phoneNumber);
        MvcResult result = postRequestWithBody(URL, clientRegisterRequest).andReturn();
        verify(clientService).register(clientRegisterRequest);
    }
}

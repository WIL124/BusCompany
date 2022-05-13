package thumbtack.buscompany.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import thumbtack.buscompany.exception.Errors;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.service.AdminService;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends RestControllerTest {

    private static final String URL = "/api/admins";
    @MockBean
    private AdminService adminService;

    @Before
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void registerAdmin_NullLogin_shouldReturn400AndMessage() throws Exception {
        AdminRegisterRequest adminRegisterRequest = getAdminRegisterRequest();
        adminRegisterRequest.setLogin(null);
        MvcResult result = postRequestWithBody(URL, adminRegisterRequest)
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        assertTrue(result.getResponse().getContentAsString().contains("message\":\"null login\""));
        assertTrue(result.getResponse().getContentAsString().contains("field\":\"login\""));
        assertEquals(1, mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().size());
    }

    public static Stream<Arguments> parametersForFioValidation() {
        return Stream.of(
                Arguments.arguments("Latin name"),
                Arguments.arguments("        "),
                Arguments.arguments("Ооооочень длинное имяяяяяяяяяяяяяяяяяяяяя" +
                        "яяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяя"),
                Arguments.arguments("Цифры в 1мени"),
                Arguments.arguments("Недопустимый символ в концеl"),
                Arguments.arguments("lНедопустимый символ в начале"),
                Arguments.arguments("Недопустимый сим1вол в середине"),
                Arguments.arguments("lНедопустимый символ в конце строки")
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForFioValidation")
    public void registerAdmin_invalidFirstname_shouldReturn400AndMessage(String name) throws Exception { //Testing Fio annotation
        AdminRegisterRequest adminRegisterRequest = getAdminRegisterRequest();
        adminRegisterRequest.setFirstName(name);
        MvcResult result = postRequestWithBody(URL, adminRegisterRequest)
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        assertFalse(result.getResponse().getContentAsString().contains("field\":\"firstname\""));
        assertEquals(1, mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().size());
    }

    @Test
    public void registerAdmin_invalidLogin_shouldReturn400_andMaxLengthMessage() throws Exception {  //Testing MaxSize annotation
        AdminRegisterRequest adminRegisterRequest = getAdminRegisterRequest();
        adminRegisterRequest.setLogin("veeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeryLongLogin");
        MvcResult result = postRequestWithBody(URL, adminRegisterRequest)
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        assertTrue(result.getResponse().getContentAsString().contains("field\":\"login\""));
        assertEquals("50 is max size", mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().stream().findFirst().get().getMessage());
        assertEquals(1, mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().size());
    }

    @Test
    public void registerAdmin_invalidPassword_shouldReturn400_andMinPasswordLengthMessage() throws Exception {  //Testing MinPasswordSize annotation
        AdminRegisterRequest adminRegisterRequest = getAdminRegisterRequest();
        adminRegisterRequest.setPassword("pass");
        MvcResult result = postRequestWithBody(URL, adminRegisterRequest)
                .andReturn();
        assertEquals(result.getResponse().getStatus(), 400);
        assertEquals("8 is min password length", mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().stream().findFirst().get().getMessage());
        assertEquals(1, mapFromJson(result.getResponse().getContentAsString(), Errors.class)
                .getErrors().size());
    }

    @Test
    public void updateAdmin_ShouldCallAdminService() throws Exception {
        AdminUpdateRequest adminUpdateRequest = getAdminUpdateRequest();
        putRequestWithBody(URL, adminUpdateRequest).andExpect(status().isOk());
        verify(adminService).update(adminUpdateRequest);
    }
}
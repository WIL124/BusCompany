package thumbtack.buscompany.endpoint;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import thumbtack.buscompany.exception.Errors;
import thumbtack.buscompany.request.LoginRequest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(controllers = SessionController.class)
public class SessionControllerTest extends RestControllerTest {
    private static final String URL = "/api/sessions";

    @Test
    public void validLoginRequest() throws Exception {
        MvcResult result = postRequestWithBody(URL, new LoginRequest("login", "password")).andReturn();
        assertEquals(result.getResponse().getStatus(), 200);
        verify(sessionService, times(1)).login(any(), any());
    }

    @Test
    public void loginRequestWithoutPasswordShouldReturn400AndHandled() throws Exception {
        MvcResult result = postRequestWithBody(URL, new LoginRequest("login", "")).andReturn();
        verify(sessionService, times(0)).login(any(),any());
        assertAll(
                () -> assertEquals(result.getResponse().getStatus(), 400),
                () -> assertEquals(result.getResolvedException().getClass(), MethodArgumentNotValidException.class),
                () -> assertEquals(mapFromJson(result.getResponse().getContentAsString(), Errors.class).getClass(), Errors.class)
        );
    }
}

package thumbtack.buscompany.endpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.service.TripService;

@WebMvcTest(controllers = TripController.class)
public class TripControllerTest extends RestControllerTest {
    private static final String URL = "/api/trips";
    @MockBean
    private TripService tripService;

    @Override
    @BeforeEach
    public void setUp() throws ServerException {
        super.setUp();
    }

    @Test
    public void test() {

    }
}

package thumbtack.buscompany.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import thumbtack.buscompany.BuscompanyApplication;
import thumbtack.buscompany.request.AdminRequest;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BuscompanyApplication.class)
@WebAppConfiguration
public abstract class RestControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;


    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    protected static AdminRequest getAdminRegisterRequest() {
        return new AdminRequest("Владислав", "Инютин", "абс", "admin", "goodLogin", "goodPassword");
    }

    protected ResultActions postRequestWithBody(String URL, Object obj) throws Exception {
        return mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(obj)));
    }
}


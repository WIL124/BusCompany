package thumbtack.buscompany.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.service.SessionService;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
public abstract class RestControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    SessionService sessionService;
    @MockBean
    UserMapper userMapper;

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    protected ResultActions postRequestWithBody(String URL, Object obj) throws Exception {
        return mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(obj)));
    }

    protected ResultActions postRequestWithBodyAndCookie(String URL, Object obj) throws Exception {
        return mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(obj))
                .header(HttpHeaders.COOKIE, "JAVASESSIONID=23"));
    }

    protected ResultActions putRequestWithBody(String URL, Object obj) throws Exception {
        return mockMvc.perform(put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(obj)));
    }
}


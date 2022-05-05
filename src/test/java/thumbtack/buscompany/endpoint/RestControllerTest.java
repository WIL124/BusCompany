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
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

    protected static AdminRegisterRequest getAdminRegisterRequest() {
        return new AdminRegisterRequest("Владислав", "Инютин", "Игоревич", "admin", "goodLogin", "goodPassword");
    }
    protected static AdminUpdateRequest getAdminUpdateRequest() {
        return new AdminUpdateRequest("Владислав", "Инютин", "Игоревич", "admin", "goodNewPass", "goodOldPass");
    }
    protected static ClientRegisterRequest getClientRegisterRequest(){
        return new ClientRegisterRequest("Яна","Никифорова", "Михайловна", "enka@gmail.com","+79087961203", "yanayana1", "goodPassword");
    }
    protected ResultActions postRequestWithBody(String URL, Object obj) throws Exception {
        return mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(obj)));
    }
    protected ResultActions putRequestWithBody(String URL, Object obj) throws Exception {
        return mockMvc.perform(put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(obj)));
    }
}


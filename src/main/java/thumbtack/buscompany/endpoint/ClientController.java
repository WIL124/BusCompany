package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.ClientService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService service;
    @PostMapping
    public UserResponse register(@Valid @RequestBody ClientRegisterRequest request, HttpServletResponse response) throws ServerException {
        return service.register(request, response);
    }
}

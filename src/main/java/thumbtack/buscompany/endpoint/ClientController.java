package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.ClientUpdateRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.ClientService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService service;
    @PostMapping
    public UserResponse register(@Valid @RequestBody ClientRegisterRequest request, HttpServletResponse response) throws ServerException {
        return service.register(request, response);
    }
    @GetMapping
    public List<UserResponse> getAllClients(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        return service.getAllClients(sessionId);
    }
    @PutMapping
    public UserResponse update(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId, @RequestBody ClientUpdateRequest request) throws ServerException {
        UserResponse response = service.updateClient(sessionId,request);
        response.setId(null);
        return response;
    }
}

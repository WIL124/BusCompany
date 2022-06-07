package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.ClientUpdateRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.ClientService;
import thumbtack.buscompany.service.SessionService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    private ClientService clientService;
    private SessionService sessionService;
    private UserMapper userMapper;

    @PostMapping
    public UserResponse register(@Valid @RequestBody ClientRegisterRequest request, HttpServletResponse response) throws ServerException {
        Client client = clientService.register(request);
        return sessionService.login(userMapper.userToLoginRequest(client), response);
    }

    @GetMapping
    public List<UserResponse> getAllClients(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            sessionService.updateTime(sessionId);
            return clientService.getAllClients();
        } else {
            throw new ServerException(ErrorCode.NOT_AN_ADMIN, JAVASESSIONID);
        }
    }

    @PutMapping
    public UserResponse update(@CookieValue(value = JAVASESSIONID) @NotNull String sessionId, @RequestBody ClientUpdateRequest request) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Client) {
            UserResponse response = clientService.updateClient(sessionId, request);
            response.setId(null);
            sessionService.updateTime(sessionId);
            return response;
        } else {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, JAVASESSIONID);
        }
    }
}

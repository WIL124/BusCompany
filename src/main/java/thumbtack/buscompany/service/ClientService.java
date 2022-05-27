package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.ClientUpdateRequest;
import thumbtack.buscompany.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService {
    private SessionService sessionService;
    private UserDao userDao;
    private UserMapper userMapper;

    public Client register(ClientRegisterRequest request) throws ServerException {
        Client client = userMapper.clientFromRequest(request);
        client.canonizePhoneFormat();
        try {
            userDao.insert(client);
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
            }
        }
        return client;
    }

    public List<UserResponse> getAllClients() {
        return userDao.getAllClients().stream()
                .map(client -> userMapper.clientToResponse(client)).collect(Collectors.toList());
    }

    public UserResponse updateClient(String sessionId, ClientUpdateRequest request) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (!request.getOldPassword().equals(user.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        Client client = (Client) user;
        userMapper.updateClientFromRequest(request, client);
        client.canonizePhoneFormat();
        userDao.updateClient(client);
        sessionService.updateTime(sessionId);
        return userMapper.clientToResponse(client);
    }
}

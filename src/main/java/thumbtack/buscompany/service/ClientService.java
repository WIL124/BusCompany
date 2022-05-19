package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService {
    SessionService sessionService;
    UserDao userDao;
    UserMapper userMapper;

    public UserResponse register(ClientRegisterRequest request, HttpServletResponse response) throws ServerException {
        if (userDao.getUserByLogin(request.getLogin()).isPresent()) {
            throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
        }
        Client client = userMapper.clientFromRegisterRequest(request);
        client.phoneNumberFormat();
        client.setUserType(UserType.CLIENT);
        userDao.insert(client);
        return sessionService.login(userMapper.userToLoginRequest(client), response);
    }

    public List<UserResponse> getAllClients(String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Client) {
            throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "userType");
        }
        sessionService.updateTime(sessionId);
        return userDao.getAllClients().stream()
                .map(client -> userMapper.clientToClientResponse(client)).collect(Collectors.toList());
    }
}

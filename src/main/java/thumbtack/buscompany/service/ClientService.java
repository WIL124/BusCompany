package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.ClientUpdateRequest;
import thumbtack.buscompany.response.ClientResponse;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ClientService extends ServiceBase {
    private UserDao userDao;
    private UserMapper userMapper;
    private SessionDao sessionDao;

    public ClientResponse register(ClientRegisterRequest request, HttpServletResponse response) throws ServerException {
        Client client = userMapper.clientFromRequest(request);
        try {
            userDao.insert(client);
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
            }
        }
        Session session = new Session(client);
        sessionDao.insert(session);
        Cookie cookie = new Cookie("JAVASESSIONID", session.getSessionId());
        response.addCookie(cookie);
        return userMapper.clientToResponse(client);
    }

    public List<UserResponse> getAllClients(String sessionId) throws ServerException {
        getAdminOrThrow(sessionId);
        return userDao.getAllClients().stream()
                .map(client -> userMapper.clientToResponse(client)).collect(Collectors.toList());
    }

    public UserResponse updateClient(String sessionId, ClientUpdateRequest request) throws ServerException {
        Client client = getClientOrThrow(sessionId);
        if (!request.getOldPassword().equals(client.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        userMapper.updateClientFromRequest(request, client);
        userDao.updateClient(client);
        return userMapper.clientToResponse(client);
    }
}

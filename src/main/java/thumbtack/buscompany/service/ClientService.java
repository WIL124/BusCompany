package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.model.User;
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
public class ClientService {
    private UserDao userDao;
    private UserMapper userMapper;
    private SessionDao sessionDao;

    public ClientResponse register(ClientRegisterRequest request, HttpServletResponse response) throws ServerException {
        Client client = userMapper.clientFromRequest(request);
        client.canonizePhoneFormat();
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
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        if (user instanceof Admin) {
            sessionDao.updateTime(sessionId);
            return userDao.getAllClients().stream()
                    .map(client -> userMapper.clientToResponse(client)).collect(Collectors.toList());
        } else throw new ServerException(ErrorCode.NOT_AN_ADMIN, "JAVASESSIONID");
    }

    public UserResponse updateClient(String sessionId, ClientUpdateRequest request) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        if (user instanceof Admin) {
            throw new ServerException(ErrorCode.NOT_A_CLIENT, "JAVASESSIONID");
        }
        if (!request.getOldPassword().equals(user.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        Client client = (Client) user;
        userMapper.updateClientFromRequest(request, client);
        client.canonizePhoneFormat();
        userDao.updateClient(client);
        UserResponse response = userMapper.clientToResponse(client);
        response.setId(null);
        sessionDao.updateTime(sessionId);
        return response;
    }
}

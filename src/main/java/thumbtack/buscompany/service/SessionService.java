package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SessionService {

    UserDao userDao;
    SessionDao sessionDao;
    UserMapper userMapper;

    public UserResponse login(LoginRequest request, HttpServletResponse response) throws ServerException {
        User user = userDao.getUserByLogin(request.getLogin());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ServerException(ErrorCode.WRONG_PASSWORD, "password");
        }
        if (!user.isActive()) {
            throw new ServerException(ErrorCode.DELETED_USER, "login");
        }
        user = user.getUserType() == UserType.ADMIN ?
                userDao.getAdminById(user.getId()) :
                userDao.getClientById(user.getId());

        Session session = createSession(user);
        sessionDao.insert(session);
        Cookie cookie = new Cookie("JAVASESSIONID", session.getSessionId());
        response.addCookie(cookie);
        return userToResponse(user);
    }

    private Session createSession(User user) {
        return new Session(user.getId(), UUID.randomUUID().toString(), new Date().getTime(), user.getUserType());
    }

    private UserResponse userToResponse(User user) {
        return (user.getUserType() == UserType.ADMIN) ?
                userMapper.adminToAdminResponse((Admin) user) :
                userMapper.clientToClientResponse((Client) user);
    }

    public void logout(String sessionId) throws ServerException {
        Session session = sessionDao.getBySessionId(sessionId);
        if (session == null) {
            throw new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID");
        }
        if (session.getUserType() == UserType.CLIENT) {
            sessionDao.delete(sessionId);
        } else {
            adminLogout(sessionId);
        }
    }

    private void adminLogout(String sessionId) throws ServerException {
        if (sessionDao.adminCount() > 1) {
            sessionDao.delete(sessionId);
        } else {
            throw new ServerException(ErrorCode.ONE_ACTIVE_ADMIN, "JAVASESSIONID");
        }
    }
}

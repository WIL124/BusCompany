package thumbtack.buscompany.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.AppProperties;
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

@Service
public class SessionService {
    public SessionService(UserDao userDao, SessionDao sessionDao, UserMapper userMapper, AppProperties properties) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
        this.userMapper = userMapper;
        this.properties = properties;
        DISCONNECT_TIME = properties.getUserIdleTimeout() * 100L;
    }

    UserDao userDao;
    SessionDao sessionDao;
    UserMapper userMapper;
    AppProperties properties;

    static long DISCONNECT_TIME;

    public ResponseEntity<UserResponse> login(LoginRequest request, HttpServletResponse response) throws ServerException {
        User user = userDao.getUserByLogin(request.getLogin()).orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND, "login"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ServerException(ErrorCode.WRONG_PASSWORD, "password");
        }
        Session session = new Session(user);
        sessionDao.insert(session);
        Cookie cookie = new Cookie("JAVASESSIONID", session.getSessionId());
        response.addCookie(cookie);
        return new ResponseEntity<>(userToResponse(user), HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String sessionId) throws ServerException {
        Session session = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID"));
        if (session.getUser().getUserType() == UserType.CLIENT ?
                sessionDao.delete(sessionId) :
                adminLogout(sessionId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else throw new ServerException(ErrorCode.NOT_FOUND, "JAVASESSIONID");
    }

    public User getUserBySessionId(String sessionId) throws ServerException {
        Session session = getSession(sessionId);
        if (new Date().getTime() - session.getLastActivityTime() > DISCONNECT_TIME) {
            sessionDao.delete(sessionId);
            throw new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID");
        }
        return session.getUser();
    }

    public void updateTime(String sessionId) {
        sessionDao.updateTime(sessionId);
    }

    protected Session getSession(String sessionId) throws ServerException {
        return sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID"));
    }

    private UserResponse userToResponse(User user) {
        return (user.getUserType() == UserType.ADMIN) ?
                userMapper.adminToResponse((Admin) user) :
                userMapper.clientToResponse((Client) user);
    }

    private boolean adminLogout(String sessionId) throws ServerException {
        if (sessionDao.adminCount() > 1) {
            return sessionDao.delete(sessionId);
        } else {
            throw new ServerException(ErrorCode.ONE_ACTIVE_ADMIN, "JAVASESSIONID");
        }
    }

}

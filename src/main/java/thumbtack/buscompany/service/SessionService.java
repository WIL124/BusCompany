package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    static final long DISCONNECT_TIME = 30 * 60 * 100;   // MIN * 60 * 100

    public UserResponse login(LoginRequest request, HttpServletResponse response) throws ServerException {
        User user = userDao.getUserByLogin(request.getLogin()).orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND, "login"));
        // REVU а не проще ли было в SQL WHERE добавить active AND active
        // и тогда будет просто null
        // неактивные нам в Java не нужны
        // кстати, проверки на null я не вижу. А если такого логина нет ?
        if (!user.isActive()) {
            throw new ServerException(ErrorCode.DELETED_USER, "login");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ServerException(ErrorCode.WRONG_PASSWORD, "password");
        }
        // REVU а зачем ? Есть же уже user, а какого он типа - для операции "логин" решительно все равно
        user = user.getUserType() == UserType.ADMIN ?
                userDao.getAdminById(user.getId()) :
                userDao.getClientById(user.getId());
        Session session = createSession(user);
        // REVU если так делать, то нужно
        // @Transactional
        // иначе не будет трансакции
        // а вообше лучше сделать немного иначе здесь, в одно действие
        // https://dev.mysql.com/doc/refman/8.0/en/insert-on-duplicate.html
        sessionDao.deleteByUserId(user.getId());
        sessionDao.insert(session);
        Cookie cookie = new Cookie("JAVASESSIONID", session.getSessionId());
        response.addCookie(cookie);
        return userToResponse(user);
    }

    public boolean logout(String sessionId) throws ServerException {
        Session session = getSession(sessionId);
        return session.getUserType() == UserType.CLIENT ?
                sessionDao.delete(sessionId) :
                adminLogout(sessionId);
    }

    public User getUserBySessionId(String sessionId) throws ServerException {
        Session session = getSession(sessionId);
        if (new Date().getTime() - session.getLastActivityTime() > DISCONNECT_TIME) {
            sessionDao.delete(sessionId);
            throw new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID");
        }
        return session.getUserType() == UserType.ADMIN ?
                userDao.getAdminById(session.getUserId()) :
                userDao.getClientById(session.getUserId());
    }

    // REVU camelCase всегда, то есть sessionId
    public void updateTime(String session_id) {
        sessionDao.updateTime(session_id);
    }

    protected Session getSession(String id) throws ServerException {
        return sessionDao.getBySessionId(id).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID"));
    }

    private Session createSession(User user) {
        return new Session(user.getId(), UUID.randomUUID().toString(), new Date().getTime(), user.getUserType());
    }

    private UserResponse userToResponse(User user) {
        return (user.getUserType() == UserType.ADMIN) ?
                userMapper.adminToAdminResponse((Admin) user) :
                userMapper.clientToClientResponse((Client) user);
    }

    private boolean adminLogout(String sessionId) throws ServerException {
        if (sessionDao.adminCount() > 1) {
            return sessionDao.delete(sessionId);
        } else {
            throw new ServerException(ErrorCode.ONE_ACTIVE_ADMIN, "JAVASESSIONID");
        }
    }

}

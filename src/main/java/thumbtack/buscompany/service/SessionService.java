package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
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

    public UserResponse login(LoginRequest request, HttpServletResponse response) {
        User user = userDao.getUserByLogin(request.getLogin());
        if (!user.getPassword().equals(request.getPassword())) {
            //TODO Выбросить ошибку
        }
        if (user.getUserType() == UserType.ADMIN) {
            user = userDao.getAdminById(user.getId());
        }
        if (user.getUserType() == UserType.CLIENT) {
            user = userDao.getClientById(user.getId());
        }
        Session session = createSession(user);
        sessionDao.insert(session);
        Cookie cookie = new Cookie("JAVASESSIONID", session.getSessionId());
        response.addCookie(cookie);
        return userToResponse(user);
    }

    private Session createSession(User user) {
        return new Session(user.getId(), UUID.randomUUID().toString(), new Date().getTime());
    }

    private UserResponse userToResponse(User user) {
        return (user.getUserType() == UserType.ADMIN) ?
                userMapper.adminToAdminResponse((Admin) user) :
                userMapper.clientToClientResponse((Client) user);
    }
}

package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Session;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class AdminService extends ServiceBase {
    private UserDao userDao;
    private UserMapper userMapper;
    private SessionDao sessionDao;

    // REVU Вы поставили @Transactional в DAO
    // хорошо, но тут Вы вызываете 2 метода, причем разных DAO
    // все верно, так и должно быть, но трансакции не будет
    // @Transactional надо ставить на методы сервисов или вообще на класс сервиса
    // а DAO пусть само по себе - методы DAO все равно только из сервисов и вызываются
    public ResponseEntity<UserResponse> register(AdminRegisterRequest request, HttpServletResponse response) throws ServerException {
        Admin admin = userMapper.adminFromRequest(request);
        try {
            userDao.insert(admin);
        } catch (RuntimeException ex) {
            // REVU ex.getCause instanceof MySQLIntegrityConstraintViolationException
            // а вообще-то у Spring есть
            // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/dao/DuplicateKeyException.html
            if (ex.getMessage().contains("Duplicate entry")) {
                throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
            }
        }
        Session session = new Session(admin);
        sessionDao.insert(session);
        Cookie cookie = new Cookie("JAVASESSIONID", session.getSessionId());
        response.addCookie(cookie);
        return new ResponseEntity<>(userMapper.adminToResponse(admin), HttpStatus.OK);
    }

    public ResponseEntity<UserResponse> update(AdminUpdateRequest request, @NotNull String sessionId) throws ServerException {
        Admin admin = getAdminOrThrow(sessionId);
        if (!request.getOldPassword().equals(admin.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        userMapper.updateAdminFromRequest(request, admin);
        userDao.updateAdmin(admin);
        AdminResponse response = userMapper.adminToResponse(admin);
        response.setId(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

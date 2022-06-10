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
public class AdminService {
    private UserDao userDao;
    private static final String JAVASESSIONID = "JAVASESSIONID";
    private UserMapper userMapper;
    private SessionDao sessionDao;

    public ResponseEntity<UserResponse> register(AdminRegisterRequest request, HttpServletResponse response) throws ServerException {
        Admin admin = userMapper.adminFromRequest(request);
        try {
            userDao.insert(admin);
        } catch (RuntimeException ex) {
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
        Admin admin = userDao.getAdminBySessionId(sessionId).orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND, JAVASESSIONID));
        if (!request.getOldPassword().equals(admin.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        userMapper.updateAdminFromRequest(request, admin);
        userDao.updateAdmin(admin);
        AdminResponse response = userMapper.adminToResponse(admin);
        response.setId(null);
        sessionDao.updateTime(sessionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

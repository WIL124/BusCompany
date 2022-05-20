package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class AdminService {
    private SessionService sessionService;
    private UserDao userDao;
    private UserMapper userMapper;

    @Transactional
    public UserResponse register(AdminRegisterRequest request, HttpServletResponse response) throws ServerException {
        if (userDao.getUserByLogin(request.getLogin()).isPresent()) {
            throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
        }
        Admin admin = userMapper.adminFromRegisterRequest(request);
        userDao.insert(admin);
        return sessionService.login(loginRequest(admin), response);
    }

    public AdminRegisterResponse update(AdminUpdateRequest request, String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (!request.getOldPassword().equals(user.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        Admin admin = (Admin) user;
        updateAdmin(admin, request);
        userDao.updateAdmin(admin);
        return userMapper.adminToAdminResponse(admin);
    }

    private LoginRequest loginRequest(User user) {
        return new LoginRequest(user.getLogin(), user.getPassword());
    }

    private void updateAdmin(Admin admin, AdminUpdateRequest request) {
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPosition(request.getPosition());
        admin.setPatronymic(request.getPatronymic());
        admin.setPassword(request.getNewPassword());
    }
}

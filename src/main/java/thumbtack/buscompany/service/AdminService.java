package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;

@Service
@AllArgsConstructor
public class AdminService {
    private UserDao userDao;
    private UserMapper userMapper;

    @Transactional
    public Admin register(AdminRegisterRequest request) throws ServerException {
        if (userDao.getUserByLogin(request.getLogin()).isPresent()) {
            throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
        }
        Admin admin = userMapper.adminFromRegisterRequest(request);
        userDao.insert(admin);
        return admin;
    }

    public Admin update(AdminUpdateRequest request, Admin admin) throws ServerException {
        if (!request.getOldPassword().equals(admin.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        updateAdmin(admin, request);
        userDao.updateAdmin(admin);
        return admin;
    }

    private void updateAdmin(Admin admin, AdminUpdateRequest request) {
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPosition(request.getPosition());
        admin.setPatronymic(request.getPatronymic());
        admin.setPassword(request.getNewPassword());
    }
}

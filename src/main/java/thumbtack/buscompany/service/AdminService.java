package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;

@Service
@AllArgsConstructor
public class AdminService {
    UserDao userDao;
    UserMapper userMapper;

    @Transactional
    public AdminRegisterResponse register(AdminRegisterRequest adminRegisterRequest) {
        Admin admin = userMapper.userFromRegisterRequest(adminRegisterRequest);
        admin.loginToLowerCase();
        admin.setUserType(UserType.ADMIN);

        userDao.insertUserProperties(admin);
        userDao.insertAdminProperties(admin);

        return userMapper.adminToAdminResponse(admin);
    }

    public AdminRegisterResponse update(AdminUpdateRequest request) {
        return null;
    }
}

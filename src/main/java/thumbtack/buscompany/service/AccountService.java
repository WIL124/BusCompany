package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.AccountDao;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.response.UserResponse;

@Service
@AllArgsConstructor
public class AccountService {
    AccountDao accountDao;
    UserMapper userMapper;

    @Transactional
    public boolean delete(User user) {
        return accountDao.deactivateUser(user.getId());
    }

    public UserResponse get(User user) {
        return user instanceof Client ?
                userMapper.clientToClientResponse((Client) user) :
                userMapper.adminToAdminResponse((Admin) user);
    }
}

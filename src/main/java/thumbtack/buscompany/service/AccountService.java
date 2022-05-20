package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.AccountDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.response.UserResponse;

@Service
@AllArgsConstructor
public class AccountService {
    AccountDao accountDao;
    SessionService sessionService;
    UserMapper userMapper;

    public ResponseEntity<Void> delete(String session_id) throws ServerException {
        User user = sessionService.getUserBySessionId(session_id);
        accountDao.deactivateUser(user.getId());
        sessionService.logout(session_id);
        return null;
    }

    public UserResponse get(String session_id) throws ServerException {
        User user = sessionService.getUserBySessionId(session_id);
        sessionService.updateTime(session_id);
        return user instanceof Client ?
                userMapper.clientToClientResponse((Client) user) :
                userMapper.adminToAdminResponse((Admin) user);
    }
}

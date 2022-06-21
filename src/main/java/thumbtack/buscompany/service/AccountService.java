package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.AccountDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.response.UserResponse;

@Service
@AllArgsConstructor
@Transactional
public class AccountService extends ServiceBase {
    AccountDao accountDao;
    UserMapper userMapper;
    SessionDao sessionDao;

    public ResponseEntity<Void> delete(String sessionId) throws ServerException {
        User user = getUserOrThrow(sessionId);
        sessionDao.delete(sessionId);
        accountDao.deactivateUser(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public UserResponse get(String sessionId) throws ServerException {
        User user = getUserOrThrow(sessionId);
        return (user instanceof Client ?
                userMapper.clientToResponse((Client) user) :
                userMapper.adminToResponse((Admin) user));
    }
}

package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.AccountDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.response.UserResponse;

@Service
@AllArgsConstructor
public class AccountService {
    AccountDao accountDao;
    UserDao userDao;
    SessionDao sessionDao;
    UserMapper userMapper;

    public ResponseEntity<Void> delete(String session_id) throws ServerException {
        Session session = sessionDao.getBySessionId(session_id);
        if (session == null) {
            throw new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID");
        }
        accountDao.delete(session.getUserId());
        return null;
    }

    public UserResponse get(String session_id) throws ServerException {
        Session session = sessionDao.getBySessionId(session_id);
        if (session == null) {
            throw new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID");
        }
        User user = userDao.getUserById(session.getUserId());
        if (!user.isActive()) {
            throw new ServerException(ErrorCode.DELETED_USER, "login");
        }
        user = (user.getUserType() == UserType.CLIENT) ?
                userDao.getClientById(user.getId()) :
                userDao.getAdminById(user.getId());

        return user instanceof Client ?
                userMapper.clientToClientResponse((Client) user) :
                userMapper.adminToAdminResponse((Admin) user);
    }
}

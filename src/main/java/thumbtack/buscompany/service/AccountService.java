package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.AccountDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.UserDao;
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

    public ResponseEntity<Void> delete(String session_id) {
        Session session = sessionDao.getBySessionId(session_id);
        if (session == null) {
            //TODO Выбросить ошибку или вернуть null?
            return null;
        }
        accountDao.delete(session.getUserId());
        return null;
    }

    public UserResponse get(String session_id) {
        Session session = sessionDao.getBySessionId(session_id);
        if (session == null) {
            //TODO Выбросить ошибку или вернуть null?
            return null;
        }
        User user = userDao.getUserById(session.getUserId());
        if (!user.isActive()) {
            return null;
        }
        user = (user.getUserType() == UserType.CLIENT) ?
                userDao.getClientById(user.getId()) :
                userDao.getAdminById(user.getId());

        return user instanceof Client ?
                userMapper.clientToClientResponse((Client) user) :
                userMapper.adminToAdminResponse((Admin) user);
    }
}

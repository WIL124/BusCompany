package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.BusDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class BusService {
    // REVU private
    SessionDao sessionDao;
    BusDao busDao;

    public List<Bus> getAll(String sessionId) throws ServerException {
        // REVU у Вас такой код во многих местах
        // у Вас же есть checkIsAdminOrThrow
        // см. REVU там
        // а чтобы он был доступен всекм сервисам, завести class ServiceBase,
        // сделать все сервисы его наследниками
        // и поместить эти 2 метода (а может, и еще какие-то)
        // туда как protected
        // Кстати, посмотрите
        // https://dzone.com/articles/ibatis-mybatis-discriminator
        // https://github.com/loiane/ibatis-discriminator
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.USER_NOT_FOUND, "JAVASESSIONID")).getUser();
        if (user instanceof Admin) {
            sessionDao.updateTime(sessionId);
            return busDao.getAll();
        } else throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
    }

    public Bus get(String name) throws ServerException {
        return busDao.get(name).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "busName"));
    }
}

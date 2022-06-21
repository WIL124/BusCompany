package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.BusDao;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Bus;

import java.util.List;

@Service
@AllArgsConstructor
public class BusService extends ServiceBase {
    private SessionDao sessionDao;
    private BusDao busDao;

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
        getAdminOrThrow(sessionId);
        return busDao.getAll();
    }

    public Bus get(String name) throws ServerException {
        return busDao.get(name).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "busName"));
    }
}

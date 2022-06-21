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
        getAdminOrThrow(sessionId);
        return busDao.getAll();
    }

    public Bus get(String name) throws ServerException {
        return busDao.get(name).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "busName"));
    }
}

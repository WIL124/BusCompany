package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.BusDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Bus;

import java.util.List;

@Service
@AllArgsConstructor
public class BusService {
    BusDao busDao;
    public List<Bus> getAll() {
        return busDao.getAll();
    }
    public Bus get(String name) throws ServerException {
        return busDao.get(name).orElseThrow(()-> new ServerException(ErrorCode.NOT_FOUND, "busName"));
    }
}

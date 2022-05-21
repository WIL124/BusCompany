package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.BusDao;
import thumbtack.buscompany.model.Bus;

import java.util.List;

@Service
@AllArgsConstructor
public class BusesService {
    BusDao busDao;
    public List<Bus> getAll() {
        return busDao.getAll();
    }
}

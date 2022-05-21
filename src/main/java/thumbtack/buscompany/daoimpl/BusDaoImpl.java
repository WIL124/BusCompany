package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.BusDao;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.repository.BusRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BusDaoImpl implements BusDao {
    BusRepository busRepository;

    @Override
    public List<Bus> getAll() {
        return busRepository.getAll();
    }
}

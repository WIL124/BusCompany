package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.BusDao;
import thumbtack.buscompany.model.Bus;
import thumbtack.buscompany.repository.BusRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Transactional
public class BusDaoImpl implements BusDao {
    BusRepository busRepository;

    @Override
    public List<Bus> getAll() {
        return busRepository.getAll();
    }

    @Override
    public Optional<Bus> get(String name) {
        return Optional.ofNullable(busRepository.get(name));
    }
}

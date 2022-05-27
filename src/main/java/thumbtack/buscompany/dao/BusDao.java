package thumbtack.buscompany.dao;

import thumbtack.buscompany.model.Bus;

import java.util.List;
import java.util.Optional;

public interface BusDao {
    List<Bus> getAll();
    Optional<Bus> get(String name);
}

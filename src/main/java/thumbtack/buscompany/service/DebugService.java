package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.DebugDao;

@Service
@AllArgsConstructor
public class DebugService {
    DebugDao debugDao;

    public ResponseEntity<Void> clear() {
        debugDao.clear();
        return null;
    }
}

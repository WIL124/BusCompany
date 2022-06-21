package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.DebugDao;

@Service
@AllArgsConstructor
@Transactional
public class DebugService {
    DebugDao debugDao;

    public ResponseEntity<Void> clear() {
        debugDao.clear();
        return null;
    }
}

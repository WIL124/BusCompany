package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.service.DebugService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/debug")
public class DebugController {
    private DebugService debugService;

    @PostMapping("/clear")
    public ResponseEntity<Void> clear() {
        return debugService.clear();
    }
}

package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.AccountService;

import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private static final String JAVA_SESSION_ID = "JAVASESSIONID";
    private AccountService accountService;

    @DeleteMapping
    public ResponseEntity<Void> delete(@CookieValue(value = JAVA_SESSION_ID) @NotNull String sessionId) throws ServerException {
        return accountService.delete(sessionId);
    }

    @GetMapping
    public UserResponse get(@CookieValue(value = JAVA_SESSION_ID) @NotNull String sessionId) throws ServerException {
        return accountService.get(sessionId);
    }
}

package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.AccountService;
import thumbtack.buscompany.service.SessionService;

import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    AccountService accountService;
    SessionService sessionService;

    @DeleteMapping
    public ResponseEntity<Void> delete(@CookieValue(value = JAVASESSIONID) @NotNull String session_id) throws ServerException {
        User user = sessionService.getUserBySessionId(session_id);
        sessionService.logout(session_id);
        accountService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponse> get(@CookieValue(value = JAVASESSIONID) @NotNull String session_id) throws ServerException {
        User user = sessionService.getUserBySessionId(session_id);
        sessionService.updateTime(session_id);
        return new ResponseEntity<>(accountService.get(user), HttpStatus.OK);
    }
}

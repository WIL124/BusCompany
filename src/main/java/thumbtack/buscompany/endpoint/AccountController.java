package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
    AccountService accountService;

    @DeleteMapping
    public ResponseEntity<Void> delete(@CookieValue(name = "JAVASESSIONID", value = "session_id") @NotNull String session_id) throws ServerException {
        return accountService.delete(session_id);
    }

    @GetMapping
    public ResponseEntity<UserResponse> get(@CookieValue(name = "JAVASESSIONID", value = "session_id") @NotNull String session_id) throws ServerException {
        return new ResponseEntity<>(accountService.get(session_id), HttpStatus.OK);
    }
}

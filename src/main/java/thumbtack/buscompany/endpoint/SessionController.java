package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.SessionService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) throws ServerException {
        return new ResponseEntity<>(sessionService.login(loginRequest, response), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> logout(@CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        sessionService.logout(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

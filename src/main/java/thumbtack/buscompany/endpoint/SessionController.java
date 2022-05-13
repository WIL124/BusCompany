package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.SessionService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {
    SessionService service;

    @PostMapping
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return new ResponseEntity<>(service.login(loginRequest, response), HttpStatus.OK);
    }

    @DeleteMapping
    public void delete() {

    }
}

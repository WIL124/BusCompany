package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.AdminService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody AdminRegisterRequest request, HttpServletResponse response) throws ServerException {
        return adminService.register(request, response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@Valid @RequestBody AdminUpdateRequest request, @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        return adminService.update(request, sessionId);
    }
}

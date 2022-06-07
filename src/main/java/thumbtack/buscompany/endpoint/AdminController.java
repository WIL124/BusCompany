package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.AdminService;
import thumbtack.buscompany.service.SessionService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private static final String JAVASESSIONID = "JAVASESSIONID";
    private AdminService adminService;
    private SessionService sessionService;
    private UserMapper mapper;

    @PostMapping
    public UserResponse register(@Valid @RequestBody AdminRegisterRequest request, HttpServletResponse response) throws ServerException {
        Admin admin = adminService.register(request);
        LoginRequest logReq = mapper.userToLoginRequest(admin);
        return sessionService.login(logReq, response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@Valid @RequestBody AdminUpdateRequest request, @CookieValue(value = "JAVASESSIONID") @NotNull String sessionId) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (user instanceof Admin) {
            adminService.update(request, (Admin) user);
        } else {
            throw new ServerException(ErrorCode.NOT_AN_ADMIN, JAVASESSIONID);
        }
        Admin admin = (Admin) user;
        AdminResponse response = mapper.adminToResponse(admin);
        response.setId(null);
        sessionService.updateTime(sessionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

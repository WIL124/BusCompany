package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.response.UserResponse;
import thumbtack.buscompany.service.AdminService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private AdminService adminService;


    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody AdminRegisterRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(adminService.register(request, response), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<UserResponse> update(@Valid @RequestBody AdminUpdateRequest request) {
        return new ResponseEntity<>(adminService.update(request), HttpStatus.OK);
    }
}

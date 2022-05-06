package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;
import thumbtack.buscompany.service.AdminService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminRegisterResponse> register(@Valid @RequestBody AdminRegisterRequest request) {
        return new ResponseEntity<>(adminService.register(request), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<AdminRegisterResponse> update(@Valid @RequestBody AdminUpdateRequest request) {
        return new ResponseEntity<>(adminService.update(request), HttpStatus.OK);
    }
}

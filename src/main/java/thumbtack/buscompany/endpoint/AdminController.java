package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
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
    public AdminRegisterResponse register(@Valid @RequestBody AdminRegisterRequest request) {
        return adminService.register(request);
    }
    @PutMapping
    public AdminRegisterResponse update(@Valid @RequestBody AdminUpdateRequest request){
        return adminService.update(request);
    }
}

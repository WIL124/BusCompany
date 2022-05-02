package thumbtack.buscompany.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thumbtack.buscompany.request.AdminRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.service.AdminService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @PostMapping("/api/admins")
    public AdminResponse register(@Valid @RequestBody AdminRequest request) {
        return adminService.register(request);
    }
}

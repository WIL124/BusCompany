package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.AdminUpdateRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;

@Service
@AllArgsConstructor
public class AdminService {

    public AdminRegisterResponse register(AdminRegisterRequest adminRegisterRequest) {
        return null;
    }

    public AdminRegisterResponse update(AdminUpdateRequest request) {
        return null;
    }
}

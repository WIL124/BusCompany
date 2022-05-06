package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Admin userFromRegisterRequest(AdminRegisterRequest request);

    AdminRegisterResponse adminToAdminResponse(Admin admin);
}

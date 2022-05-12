package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.response.AdminRegisterResponse;
import thumbtack.buscompany.response.ClientRegisterResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Admin adminFromRegisterRequest(AdminRegisterRequest request);

    AdminRegisterResponse adminToAdminResponse(Admin admin);

    Client clientFromRegisterRequest(AdminRegisterRequest request);

    ClientRegisterResponse clientToClientResponse(Client client);
}

package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.AdminRegisterRequest;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.LoginRequest;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.ClientResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Admin adminFromRegisterRequest(AdminRegisterRequest request);

    AdminResponse adminToAdminResponse(Admin admin);

    Client clientFromRegisterRequest(ClientRegisterRequest request);

    ClientResponse clientToClientResponse(Client client);

    LoginRequest userToLoginRequest(User user);
}
